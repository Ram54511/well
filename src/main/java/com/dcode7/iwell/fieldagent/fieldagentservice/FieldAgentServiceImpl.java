package com.dcode7.iwell.fieldagent.fieldagentservice;

import com.dcode7.iwell.CNF.inventoryrequesthistory.InventoryRequestHistory;
import com.dcode7.iwell.CNF.inventoryrequesthistory.InventoryRequestHistoryRepository;
import com.dcode7.iwell.common.exception.CustomException;
import com.dcode7.iwell.common.exception.InsufficientStockException;
import com.dcode7.iwell.common.exception.ItemNotFoundException;
import com.dcode7.iwell.common.exception.ResourceNotFoundException;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.fieldagent.CNFInventoryItemDTO;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.UserRepository;
import com.dcode7.iwell.user.enums.UserType;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItemRepository;
import com.dcode7.iwell.user.inventory.inventoryitem.service.InventoryItemService;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequestDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequestRepository;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetail;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetailDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetailRepository;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.service.InventoryRequestService;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.service.InventoryRequestServiceImpl;
import com.dcode7.iwell.user.invoice.Invoice;
import com.dcode7.iwell.user.invoice.InvoiceRepository;
import com.dcode7.iwell.user.invoice.InvoiceStatus;
import com.dcode7.iwell.user.invoice.invoiceDetail.InvoiceDetail;
import com.dcode7.iwell.utils.GenericUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FieldAgentServiceImpl implements FieldAgentService {

    private final InventoryRequestRepository inventoryRequestRepository;

    private final InventoryRequestService inventoryRequestService;

    private final InventoryRequestDetailRepository inventoryRequestDetailRepository;

    private final InventoryRequestHistoryRepository inventoryRequestHistoryRepository;

    private final UserRepository userRepository;

    private final InventoryItemService inventoryItemService;

    private final LocalService localService;

    private final InventoryItemRepository inventoryItemRepository;

    private final InvoiceRepository invoiceRepository;

    private static final Logger log = LoggerFactory.getLogger(FieldAgentServiceImpl.class);

    @Override
    public List<CNFInventoryItemDTO> getInventoryItemOfCnf() {
        User user = GenericUtils.getLoggedInUser();
        User userObj = user.getReferencedUser();

        if (userObj != null) {
            List<InventoryRequest> inventoryRequests = getInventoryRequests(userObj);
            List<InventoryRequestDetail> sortedInventoryRequestDetails = sortInventoryRequestDetails(inventoryRequests);

            return mapToDTOs(sortedInventoryRequestDetails);
        } else {
            List<InventoryItem> inventoryRequestDetails = inventoryItemService.getAllInventoryItem();
            return mapToDTOsOfAdmin(inventoryRequestDetails);
        }


    }


    /**
     * Fetches the inventory requests for the given user with approved status.
     *
     * @param user the user for whom to fetch the inventory requests
     * @return the list of inventory requests
     */
    private List<InventoryRequest> getInventoryRequests(User user) {
        return inventoryRequestRepository.getInventoryRequestItemByRequestedByAndInventoryStatus(user, InventoryStatus.APPROVED);
    }

    /**
     * Flattens the list of inventory requests to their details and sorts them by expiry date and register date.
     *
     * @param inventoryRequests the list of inventory requests
     * @return the sorted list of inventory request details
     */
    private List<InventoryRequestDetail> sortInventoryRequestDetails(List<InventoryRequest> inventoryRequests) {
        return inventoryRequests.stream().flatMap(request -> request.getInventoryRequestDetail().stream()).sorted(Comparator.comparing((InventoryRequestDetail detail) -> detail.getRequestedItem().getExpiryDate(), Comparator.nullsLast(Comparator.naturalOrder())).thenComparing(detail -> detail.getRequestedItem().getRegisterDate(), Comparator.nullsLast(Comparator.naturalOrder()))).collect(Collectors.toList());
    }

    /**
     * Maps the sorted inventory request details to CNFInventoryItemDTOs.
     *
     * @param sortedInventoryRequestDetails the sorted list of inventory request details
     * @return the list of CNFInventoryItemDTOs
     */
    private List<CNFInventoryItemDTO> mapToDTOs(List<InventoryRequestDetail> sortedInventoryRequestDetails) {
        List<CNFInventoryItemDTO> dtos = new ArrayList<>();
        for (InventoryRequestDetail inventoryRequestDetail : sortedInventoryRequestDetails) {
            CNFInventoryItemDTO dto = new CNFInventoryItemDTO();
            InventoryItem requestedItem = inventoryRequestDetail.getRequestedItem();
            dto.setInventoryId(requestedItem.getId());
            dto.setInventoryRequest(inventoryRequestDetail.getInventoryRequest().getRequestId());
            dto.setCnf(inventoryRequestDetail.getInventoryRequest().getRequestedBy().getFullName());
            dto.setName(requestedItem.getName());
            dto.setDescription(requestedItem.getDescrition());
            dto.setExpiryDate(requestedItem.getExpiryDate());
            dto.setHSNCode(requestedItem.getHSNCode());
            dto.setSellingPrice(requestedItem.getSellingPrice());
            dto.setStorageCondition(requestedItem.getStorageCondition());
            dto.setUnitCost(requestedItem.getUnitCost());
            dto.setNoOfStock(requestedItem.getNoOfStock());
            dto.setStockKeepingUnit(requestedItem.getStockKeepingUnit());
            dto.setManufacturer(requestedItem.getManufacturer());
            dto.setRegisterDate(requestedItem.getRegisterDate());
            dtos.add(dto);
        }
        return dtos;
    }

    //get all the inventory item of inventory
    private List<CNFInventoryItemDTO> mapToDTOsOfAdmin(List<InventoryItem> sortedInventoryRequestDetails) {
        List<CNFInventoryItemDTO> dtos = new ArrayList<>();
        for (InventoryItem requestedItem : sortedInventoryRequestDetails) {
            CNFInventoryItemDTO dto = new CNFInventoryItemDTO();
            dto.setInventoryId(requestedItem.getId());
            dto.setName(requestedItem.getName());
            dto.setDescription(requestedItem.getDescrition());
            dto.setExpiryDate(requestedItem.getExpiryDate());
            dto.setHSNCode(requestedItem.getHSNCode());
            dto.setSellingPrice(requestedItem.getSellingPrice());
            dto.setStorageCondition(requestedItem.getStorageCondition());
            dto.setUnitCost(requestedItem.getUnitCost());
            dto.setRegisterDate(requestedItem.getRegisterDate());
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    @CacheEvict(value = "inventoryRequests", key = "#id")
    public void approvedRequestByCNF(UUID id, InventoryStatus inventoryStatus) {
        InventoryRequest inventoryRequestObj = findAndValidateRequest(id);
        User user = GenericUtils.getLoggedInUser();
        User userRefrence = user.getReferencedUser();
        InventoryStatus currentStatus = inventoryRequestObj.getInventoryStatus();
        if (currentStatus == InventoryStatus.PENDING && inventoryStatus == InventoryStatus.APPROVED) {
            throw new CustomException(localService.getMessage("request.must.be.processed.first"), HttpStatus.BAD_REQUEST);
        }
        if (currentStatus != InventoryStatus.APPROVED) {
            createInventoryRequestHistoryApproved(inventoryRequestObj, user, inventoryStatus);
            if (inventoryStatus == InventoryStatus.APPROVED) {
                InventoryRequest requestCnf = inventoryRequestRepository.getInventoryByRequestedTo(userRefrence);
//                System.out.println("============================"+requestCnf.getRequestedBy()+"============================"+requestCnf.getRequestedTo()+"============================");
                updateInventoryStock(inventoryRequestObj, requestCnf);
                inventoryRequestObj.setInventoryStatus(inventoryStatus);
                createInvoices(inventoryRequestObj, user);
            } else {
                inventoryRequestObj.setInventoryStatus(inventoryStatus);
            }
            inventoryRequestRepository.save(inventoryRequestObj);
        } else {
            throw new CustomException(localService.getMessage("already.approved"), HttpStatus.BAD_REQUEST);
        }
    }


    private void createInventoryRequestHistoryApproved(InventoryRequest inventoryRequest, User user, InventoryStatus inventoryStatus) {
        InventoryRequestHistory inventoryRequestHistory = new InventoryRequestHistory();
        inventoryRequestHistory.setInventoryRequest(inventoryRequest);
        inventoryRequestHistory.setInventoryStatus(inventoryStatus);
        inventoryRequestHistory.setStatusUpdateBy(user);
        inventoryRequestHistoryRepository.save(inventoryRequestHistory);
    }

    private void validateInventoryAvailability(InventoryRequest inventoryRequest) {
        List<InventoryRequestDetail> inventoryRequestDetailList = inventoryRequest.getInventoryRequestDetail();
        for (InventoryRequestDetail detail : inventoryRequestDetailList) {
            InventoryItem requestedItem = detail.getRequestedItem();
            Integer requestedQuantity = detail.getQuantity();
            if (requestedQuantity > requestedItem.getNoOfStock()) {
                throw new InsufficientStockException(
                        localService.getMessage("inventory.item.insufficient", requestedItem.getName()), HttpStatus.BAD_REQUEST);
            }
        }
    }


    // validate the inventory item is available or not
    private InventoryRequest findAndValidateRequest(UUID id) throws ResourceNotFoundException {
        return inventoryRequestRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(localService.getMessage("inventory.item.not.found"), HttpStatus.NOT_FOUND));
    }

    private void updateInventoryStock(InventoryRequest inventoryRequest, InventoryRequest inventoryRequestCnf) {
        if (inventoryRequestCnf == null) {
            throw new CustomException(localService.getMessage("cnf.request.not.found"), HttpStatus.BAD_REQUEST);
        }
        List<InventoryRequestDetail> inventoryRequestDetailList = inventoryRequest.getInventoryRequestDetail();
        List<InventoryRequestDetail> inventoryRequestDetailCnfList = inventoryRequestCnf.getInventoryRequestDetail();
        for (InventoryRequestDetail detail : inventoryRequestDetailList) {
            for (InventoryRequestDetail detailCnf : inventoryRequestDetailCnfList) {
                if (detail.getRequestedItem().getId().equals(detailCnf.getRequestedItem().getId())) {
                    Integer requestedQuantity = detail.getQuantity();
                    Integer availableStock = detailCnf.getQuantity();
                    updateStockForItem(detailCnf, requestedQuantity, availableStock);
                    break;
                }
            }
        }
    }

    /**
     * Updates the stock for each InventoryItem associated with the given InventoryRequest.
     */
    private void updateStockForItem(InventoryRequestDetail detailCnf, Integer requestedQuantity, Integer availableStock) {
        Integer subtract = availableStock - requestedQuantity;
        if (requestedQuantity <= availableStock) {
            detailCnf.setQuantity(subtract);
            inventoryRequestDetailRepository.save(detailCnf);
        } else {
            throw new InsufficientStockException(localService.getMessage("inventory.item.insufficient"), HttpStatus.BAD_REQUEST);
        }
    }

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
            detail.setInvoiceAmount(requestDetail.getRequestedItem().getSellingPrice());
            detail.setInvoiceQuantity(requestDetail.getQuantity());
            BigDecimal detailTotalAmount = requestDetail.getRequestedItem().getSellingPrice().multiply(BigDecimal.valueOf(requestDetail.getQuantity()));
            totalAmount = totalAmount.add(detailTotalAmount);
        }
        invoice.setInvoiceDetails(invoiceDetails);
        invoice.setTotalAmount(totalAmount);
        return invoiceRepository.save(invoice);
    }

    @Override
    public InventoryRequest createRequest(InventoryRequestDTO inventoryRequest) {
        return createOrder(inventoryRequest);
    }

    private InventoryRequest createOrder(InventoryRequestDTO inventoryRequestDTO) {
        User user = GenericUtils.getLoggedInUser();
        User userObj = user.getReferencedUser();
        InventoryRequest request = initializeInventoryRequest(user);
        validateHaveAdminOrReferenceUser(user, request);
        InventoryRequest savedRequest = inventoryRequestRepository.save(request);
        List<InventoryRequestDetail> details = prepareRequestDetails(savedRequest, inventoryRequestDTO, userObj);
        savedRequest.setInventoryRequestDetail(details);
        InventoryRequest updatedRequest = inventoryRequestRepository.save(savedRequest);
        return updatedRequest;
    }

    private InventoryRequest initializeInventoryRequest(User user) {
        InventoryRequest request = new InventoryRequest();
        request.setInventoryStatus(InventoryStatus.PENDING);
        request.setRequestedBy(user);
        return request;
    }

    private List<InventoryRequestDetail> prepareRequestDetails(InventoryRequest request, InventoryRequestDTO inventoryRequestDTO, User userObj) {
        List<InventoryRequestDetail> details = new ArrayList<>();
        InventoryRequest inventoryRequestCnfList = inventoryRequestRepository.getInventoryRequestByRequestedBy(userObj);
        List<InventoryRequestDetail> inventoryRequestDetail = inventoryRequestCnfList.getInventoryRequestDetail();
        log.info("Received InventoryRequestDTO: {}", inventoryRequestDTO);
        for (InventoryRequestDetailDTO requestDetailDto : inventoryRequestDTO.getInventoryRequestDetail()) {
            InventoryRequestDetail requestRetail = findMatchingRequestDetail(requestDetailDto, inventoryRequestDetail);
            if (requestRetail == null) {
                String errorMessage = localService.getMessage("unavailable.item", new Object[]{requestDetailDto.getInventoryItemId()}, Locale.getDefault());
                throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);
            }
            if (requestRetail.getQuantity() < requestDetailDto.getQuantity()) {
                String errorMessage = MessageFormat.format(localService.getMessage("inventory.item.insufficient.detail", Locale.getDefault()),
                        requestDetailDto.getInventoryItemId(),
                        requestRetail.getQuantity(),
                        requestDetailDto.getQuantity());
                throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);
            }
            InventoryRequestDetail detail = new InventoryRequestDetail();
            detail.setQuantity(requestDetailDto.getQuantity());
            detail.setRequestedItem(requestRetail.getRequestedItem());
            detail.setInventoryRequest(request);
            details.add(detail);
        }
        inventoryRequestDetailRepository.saveAll(details);
        return details;
    }

    private InventoryRequestDetail findMatchingRequestDetail(InventoryRequestDetailDTO requestDetailDto, List<InventoryRequestDetail> inventoryRequestDetail) {
        for (InventoryRequestDetail requestRetail : inventoryRequestDetail) {
            if (requestRetail.getRequestedItem().getId().equals(requestDetailDto.getInventoryItemId())) {
                return requestRetail;
            }
        }
        return null;
    }


    //update the history
    private void createInventoryrequestHistory(InventoryRequest inventoryRequest, User user) {
        InventoryRequestHistory inventoryRequestHistory = new InventoryRequestHistory();
        inventoryRequestHistory.setInventoryRequest(inventoryRequest);
        inventoryRequestHistory.setInventoryStatus(InventoryStatus.PENDING);
        inventoryRequestHistory.setStatusUpdateBy(user);
        inventoryRequestHistoryRepository.save(inventoryRequestHistory);
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

    /**
     * Finds and returns an admin user.
     *
     * @return The admin user found.
     */
    public User findAdmin() {
        User userRequestTO = userRepository.findByUserType(UserType.ADMIN);
        return userRequestTO;
    }
}
