package com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.service;

import java.math.BigDecimal;
import java.util.*;

import com.dcode7.iwell.CNF.inventoryrequesthistory.InventoryRequestHistory;
import com.dcode7.iwell.CNF.inventoryrequesthistory.InventoryRequestHistoryRepository;
import com.dcode7.iwell.common.exception.*;
import com.dcode7.iwell.user.UserRepository;
import com.dcode7.iwell.user.enums.UserType;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetailDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetailRepository;
import com.dcode7.iwell.user.invoice.Invoice;
import com.dcode7.iwell.user.invoice.InvoiceRepository;
import com.dcode7.iwell.user.invoice.InvoiceStatus;
import com.dcode7.iwell.user.invoice.invoiceDetail.InvoiceDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItemRepository;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequestDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequestRepository;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetail;
import com.dcode7.iwell.utils.GenericUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryRequestServiceImpl implements InventoryRequestService {
    private final InventoryRequestRepository inventoryRequestRepository;

    private final LocalService localService;

    private final UserRepository userRepository;

    private final InventoryItemRepository inventoryItemRepository;

    private final InventoryRequestHistoryRepository inventoryRequestHistoryRepository;

    private final InvoiceRepository invoiceRepository;

    private final InventoryRequestDetailRepository inventoryRequestDetailRepository;

    private static final Logger log = LoggerFactory.getLogger(InventoryRequestServiceImpl.class);

    @Override
    public InventoryRequest createRequest(InventoryRequestDTO inventoryRequest) {
        return createOrder(inventoryRequest);
    }

    private InventoryRequest createOrder(InventoryRequestDTO inventoryRequest) {
        User user = GenericUtils.getLoggedInUser();
        InventoryRequest request = new InventoryRequest();
        request.setInventoryStatus(InventoryStatus.PENDING);
        request.setRequestedBy(user);
        //check the login user have cnf user or not
        validateHaveAdminOrReferenceUser(user, request);
        List<InventoryRequestDetail> details = new ArrayList<>();
        log.info("Received InventoryRequestDTO: {}", inventoryRequest);
        inventoryRequest.getInventoryRequestDetail().forEach(requestDetailDto -> {
            log.info("Processing InventoryRequestDetailDto: {}", requestDetailDto);
            InventoryRequestDetail detail = new InventoryRequestDetail();
            InventoryItem inventoryItem = inventoryItemRepository.findById(requestDetailDto.getInventoryItemId()).orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("inventory.item.not.found"), HttpStatus.NOT_FOUND));
            detail.setQuantity(requestDetailDto.getQuantity());
            detail.setRequestedItem(inventoryItem);
            detail.setInventoryRequest(request);
            InventoryRequestDetail savedDetail = inventoryRequestDetailRepository.save(detail);
            details.add(detail);
            log.info("Saved InventoryRequestDetail with Quantity: {}", savedDetail.getQuantity());
        });
        request.setInventoryRequestDetail(details);
        InventoryRequest savedDetails = inventoryRequestRepository.save(request);
        createInventoryrequestHistory(savedDetails, user);
        log.info("Saved InventoryRequest with ID: {} and Details: {}", savedDetails.getRequestId(), savedDetails.getInventoryRequestDetail());
        return savedDetails;
    }


    @Override
    /**
     * Updates an existing inventory request in the system.
     *
     * @param id               The unique identifier of the inventory request to
     *                         update.
     * @param inventoryRequest The updated inventory request details.
     * @throws CustomException If the inventory request with the specified
     *                                   ID is not found.
     */
    @CacheEvict(value = "inventoryRequests", key = "#inventoryRequestId")
    public InventoryRequest updateRequest(UUID inventoryRequestId, InventoryRequestDTO inventoryRequestDto) {
        User user = GenericUtils.getLoggedInUser();
        InventoryRequest existingRequest = getExistingInventoryRequest(inventoryRequestId);
        validateRequestStatus(existingRequest);
        updateExistingRequestDetails(existingRequest, inventoryRequestDto, user);
        InventoryRequest savedRequest = inventoryRequestRepository.save(existingRequest);
        createInventoryrequestHistory(savedRequest, user);
        return savedRequest;
    }


    @Override
    /**
     * Updates an existing inventory request in the system if the admin or cnf
     * approves it.
     *
     * @param id               The unique identifier of the inventory request to
     *                         update.
     * @param inventoryRequest The updated inventory request details.
     * @throws CustomException If the inventory request with the specified
     *                                   ID is not found.
     */
    @CacheEvict(value = "inventoryRequests", key = "#id")
    public void approvedRequest(UUID id, InventoryStatus inventoryStatus) {
        InventoryRequest inventoryRequestObj = findAndValidateRequest(id);
        User user = GenericUtils.getLoggedInUser();
        InventoryStatus currentStatus = inventoryRequestObj.getInventoryStatus();
        // Validate the status transition
        if (currentStatus == InventoryStatus.PENDING && inventoryStatus == InventoryStatus.APPROVED) {
            throw new CustomException(localService.getMessage("request.must.be.processed.first"), HttpStatus.BAD_REQUEST);
        }
        if (!(inventoryRequestObj.getInventoryStatus() == InventoryStatus.APPROVED)) {
            createInventoryrequestHistoryApproved(inventoryRequestObj, user, inventoryStatus);
            if (inventoryStatus == InventoryStatus.APPROVED) {
                updateInventoryStock(inventoryRequestObj);
                inventoryRequestObj.setInventoryStatus(inventoryStatus);
                createInvoices(inventoryRequestObj, user);
            } else {
                inventoryRequestObj.setInventoryStatus(inventoryStatus);
            }
        } else {
            throw new CustomException(localService.getMessage("already.approved"), HttpStatus.BAD_REQUEST);
        }

    }

//    private InventoryRequest mergeExistingInventoryItemRequest(User user, InventoryRequest inventoryRequest){
//         User userDetail = inventoryRequest.getRequestedBy();
//         List<InventoryRequestDetail> inventoryRequestObj = inventoryRequest.getInventoryRequestDetail();
//         if(user.getId().equals(userDetail.getId())){
//            for(InventoryRequestDetail inventoryRequestDetail : inventoryRequestObj){
//                Optional<InventoryRequestDetail> existingDetail = inventoryRequestDetailRepository.findByInventoryRequestAndRequestedItem(inventoryRequest, inventoryRequestDetail.getRequestedItem());
//                if(existingDetail.isPresent()){
//                    InventoryRequestDetail existingRequestDetail = existingDetail.get();
//                    existingRequestDetail.setQuantity(existingRequestDetail.getQuantity() + inventoryRequestDetail.getQuantity());
//                    existingRequestDetail.setQuantity(existingRequestDetail.getQuantity() + inventoryRequestDetail.getQuantity());
//                    BigDecimal sellingPrice = existingRequestDetail.getRequestedItem().getSellingPrice();
//                    BigDecimal quantity = BigDecimal.valueOf(existingRequestDetail.getQuantity());
//                    BigDecimal totalAmount = sellingPrice.multiply(quantity);
//                    existingRequestDetail.setTotalAmount(totalAmount);
//                    inventoryRequestObj.remove(inventoryRequestDetail);
//                }else{
//                    new InventoryRequestDetail();
//                }
//            }
//
//         }else{
//             throw  new CustomException(localService.getMessage(""),HttpStatus.BAD_REQUEST);
//         }
//         return inventoryRequestRepository.save(inventoryRequest);
//    }


    // validate the inventory item is available or not
    private InventoryRequest findAndValidateRequest(UUID id) throws ResourceNotFoundException {
        return inventoryRequestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("inventory.item.not.found"), HttpStatus.NOT_FOUND));
    }

    // update the inventory request by user
    private void updateInventoryStock(InventoryRequest inventoryRequest) {
        List<InventoryRequestDetail> inventoryRequestDetailList = inventoryRequest.getInventoryRequestDetail();
        for (InventoryRequestDetail detail : inventoryRequestDetailList) {
            updateStockForItem(detail.getRequestedItem().getId(), detail.getQuantity());
        }
    }

    private void updateStockForItem(UUID requestId, Integer requestedQuantity) {
        log.info("Updating stock for request ID: {}, Quantity: {}", requestId, requestedQuantity);
        Optional<InventoryItem> optionalInventoryItem = inventoryItemRepository.findById(requestId);
        if (optionalInventoryItem.isPresent()) {
            InventoryItem inventoryItem = optionalInventoryItem.get();
            Integer availableStock = inventoryItem.getNoOfStock();
            Integer subtract = availableStock - requestedQuantity;
            log.info("Available stock before update: {}", availableStock);
            log.info("Available stock after update: {}", subtract);
            if (requestedQuantity <= availableStock) {
                inventoryItem.setNoOfStock(subtract);
                inventoryItemRepository.save(inventoryItem);
                log.info("Updated stock: {}", inventoryItem.getNoOfStock());
            } else {
                throw new InsufficientStockException(localService.getMessage("inventory.item.insufficient"), HttpStatus.NOT_FOUND);
            }
        } else {
            throw new ItemNotFoundException(localService.getMessage("inventory.item.not.found"), HttpStatus.NOT_FOUND);
        }
    }

    //update the history
    private void createInventoryrequestHistory(InventoryRequest inventoryRequest, User user) {
        InventoryRequestHistory inventoryRequestHistory = new InventoryRequestHistory();
        inventoryRequestHistory.setInventoryRequest(inventoryRequest);
        inventoryRequestHistory.setInventoryStatus(InventoryStatus.PENDING);
        inventoryRequestHistory.setStatusUpdateBy(user);
        inventoryRequestHistoryRepository.save(inventoryRequestHistory);
    }

    private void createInventoryrequestHistoryApproved(InventoryRequest inventoryRequest, User user, InventoryStatus inventoryStatus) {
        InventoryRequestHistory inventoryRequestHistory = new InventoryRequestHistory();
        inventoryRequestHistory.setInventoryRequest(inventoryRequest);
        inventoryRequestHistory.setInventoryStatus(inventoryStatus);
        inventoryRequestHistory.setStatusUpdateBy(user);
        inventoryRequestHistoryRepository.save(inventoryRequestHistory);
    }


    private InventoryRequest getExistingInventoryRequest(UUID inventoryRequestId) {
        return inventoryRequestRepository.findById(inventoryRequestId).orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("inventory.item.not.found"), HttpStatus.NOT_FOUND));
    }

    private void throwUnauthorizedAccessException(String message) {
        throw new UnauthorizedAccessException(localService.getMessage(message), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    private InventoryItem findInventoryItemById(UUID itemId) {
        return inventoryItemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("inventory.item.not.found"), HttpStatus.NOT_FOUND));
    }

    private void validateRequestStatus(InventoryRequest existingRequest) {
        if (existingRequest.getInventoryStatus() != InventoryStatus.PENDING) {
            throwUnauthorizedAccessException("not.modifiable");
        }
    }

    private void updateExistingRequestDetails(InventoryRequest existingRequest, InventoryRequestDTO inventoryRequestDto, User user) {
        existingRequest.setRequestedBy(user);
        existingRequest.setInventoryStatus(InventoryStatus.PENDING);

        List<InventoryRequestDetailDTO> detailDTOs = inventoryRequestDto.getInventoryRequestDetail();

        for (InventoryRequestDetailDTO detailDTO : detailDTOs) {
            InventoryItem requestItem = findInventoryItemById(detailDTO.getInventoryItemId());

            Optional<InventoryRequestDetail> existingDetailOptional = getExistingDetailByItemId(existingRequest, detailDTO.getInventoryItemId());

            if (existingDetailOptional.isPresent()) {
                InventoryRequestDetail existingDetail = existingDetailOptional.get();
                existingDetail.setQuantity(detailDTO.getQuantity());
//                existingDetail.setTotalAmount(BigDecimal.valueOf(detailDTO.getQuantity()).multiply(requestItem.getSellingPrice()));
                existingDetail.setRequestedItem(requestItem);
            }
        }
    }

    private Optional<InventoryRequestDetail> getExistingDetailByItemId(InventoryRequest existingRequest, UUID itemId) {
        return existingRequest.getInventoryRequestDetail().stream().filter(detail -> detail.getRequestedItem().getId().equals(itemId)).findFirst();
    }


    /**
     * Creates invoices based on the provided inventory request and user.
     * Calculates the total amount for each invoice based on the inventory request details.
     *
     * @param inventoryRequest The inventory request for which invoices are to be created.
     * @param user             The user associated with the invoices.
     * @return The created invoice.
     */
    private Invoice createInvoices(InventoryRequest inventoryRequest, User user) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(new Date());
        invoice.setUser(user);
        invoice.setStatus(InvoiceStatus.DUE);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<InvoiceDetail> invoiceDetails = new ArrayList<>();
        for (InventoryRequestDetail requestDetail : inventoryRequest.getInventoryRequestDetail()) {
            InvoiceDetail detail = new InvoiceDetail();
            detail.setInventoryItem(requestDetail.getRequestedItem());
//            detail.setInvoiceAmount(requestDetail.getTotalAmount());
            detail.setInvoiceQuantity(requestDetail.getQuantity());
            totalAmount = totalAmount.add(requestDetail.getRequestedItem().getUnitCost());
            invoiceDetails.add(detail);
        }
        invoice.setInvoiceDetails(invoiceDetails);
        invoice.setTotalAmount(totalAmount);
        return invoiceRepository.save(invoice);
    }


    /**
     * Validates if the user has an admin or reference user.
     * If the user has a reference user, sets the requested to the reference user.
     * If the user does not have a reference user, sets the requested to an admin user.
     *
     * @param user    The user making the request.
     * @param request The inventory request.
     */
    private void validateHaveAdminOrReferenceUser(User user, InventoryRequest request) {
        if (user.getReferencedUser() != null) {
            request.setRequestedTo(user.getReferencedUser());
        } else {
            User userRequestTO = findAdmin();
            request.setRequestedTo(userRequestTO);
        }
    }

    @Override
    /**
     * Finds and returns an admin user.
     *
     * @return The admin user found.
     */ public User findAdmin() {
        User userRequestTO = userRepository.findByUserType(UserType.ADMIN);
        return userRequestTO;
    }

}
