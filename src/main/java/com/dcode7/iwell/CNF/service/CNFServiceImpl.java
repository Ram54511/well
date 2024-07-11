package com.dcode7.iwell.CNF.service;

import com.dcode7.iwell.CNF.*;
import com.dcode7.iwell.common.exception.CustomException;
import com.dcode7.iwell.common.exception.ResourceNotFoundException;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.UserRepository;
import com.dcode7.iwell.user.enums.UserRequestStatus;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItemDto;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.CNFInventoryRequestDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.CNFInventoryRequestDetailDTO;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequestRepository;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetail;
import com.dcode7.iwell.utils.GenericUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CNFServiceImpl implements CNFService {

    private final UserRepository userRepository;

    private final InventoryRequestRepository inventoryRequestRepository;

    private final LocalService localService;

    @Override
    /**
     * Retrieves all field agent users according to cnf user.
     *
     * @return The field agent user.
     * @throws CustomException If no field agent user is found of cnf user.
     */
    public List<CNFUserDTO> getAllFieldAgent() {
        User user = GenericUtils.getLoggedInUser();
        List<User> userDetail = userRepository.findByReferencedUserAndUserRequestStatus(user, UserRequestStatus.APPROVED);
        if (userDetail == null) {
            throw new CustomException(localService.getMessage("no.any.field.agent.user"), HttpStatus.BAD_REQUEST);
        }
        List<CNFUserDTO> fieldAgentDTOs = new ArrayList<>();
        for (User fieldAgentUser : userDetail) {
            CNFUserDTO dto = convertToCNFUserDTO(fieldAgentUser);
            fieldAgentDTOs.add(dto);
        }
        return fieldAgentDTOs;
    }

    @Override
    /**
     * Retrieves the list of inventory requests for the logged-in user along with their details.
     *
     * @return List of inventory requests for the logged-in user
     * @throws CustomException if no inventory requests are found
     */
    @Cacheable(value = "userRequestItems", key = "#status")
    public List<InventoryRequestResponseDTO> getRequestItemListOfLoginUser(InventoryStatus status) {
        User user = GenericUtils.getLoggedInUser();
        System.out.println(user.getUserType());
        List<InventoryRequest> userRequests = inventoryRequestRepository.getInventoryRequestsByRequestedByAndInventoryStatus(user, status);
        if (userRequests.isEmpty()) {
            throw new CustomException(localService.getMessage("inventory.request.not.found"), HttpStatus.BAD_REQUEST);
        }
        List<InventoryRequestResponseDTO> requestDtos = new ArrayList<>();
        for (InventoryRequest request : userRequests) {
            User userRequest = request.getRequestedTo();
            // Fetch InventoryRequestDetail objects for the current InventoryRequest
            List<InventoryRequestDetail> inventoryRequestDetails = request.getInventoryRequestDetail();
            InventoryRequestResponseDTO dto = convertToDtoInventoryRequest(userRequest, request, inventoryRequestDetails);
            requestDtos.add(dto);
        }
        return requestDtos;
    }

    @Override
/**
 * Retrieves the inventory request with the specified ID and converts it to a CNFInventoryRequestDTO.
 *
 * @param inventoryRequestId The ID of the inventory request to retrieve
 * @return CNFInventoryRequestDTO representing the retrieved inventory request
 * @throws ResourceNotFoundException if the inventory request is not found
 */
    @Cacheable(value = "inventoryRequestById", key = "#inventoryRequestId")
    public CNFInventoryRequestDTO getInventoryRequestsByInventoryRequestId(UUID inventoryRequestId) {
        InventoryRequest inventoryRequest = inventoryRequestRepository.findByRequestId(inventoryRequestId);
        if (inventoryRequest != null) {
            return convertToDTO(inventoryRequest);
        } else {
            throw new ResourceNotFoundException(localService.getMessage("inventory.request.not.found"), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public List<InventoryItemRequestListOfCnfDTO> getAllRequestOfCNF() {
        User user = GenericUtils.getLoggedInUser();
        List<InventoryRequest> inventoryRequests = inventoryRequestRepository.getInventoryRequestByRequestedTo(user);

        return inventoryRequests.stream().map(this::convertToInventoryItemRequestListOfCnfDTO).collect(Collectors.toList());
    }

    /**
     * Converts an InventoryRequest entity to an InventoryItemRequestListOfCnfDTO.
     *
     * @param inventoryRequest The inventory request entity to convert
     * @return InventoryItemRequestListOfCnfDTO representing the inventory request
     */
    private InventoryItemRequestListOfCnfDTO convertToInventoryItemRequestListOfCnfDTO(InventoryRequest inventoryRequest) {
        List<InventoryRequestDetailDto> detailDtos = inventoryRequest.getInventoryRequestDetail().stream()
                .map(this::convertToInventoryRequestDetailDto)
                .collect(Collectors.toList());
        CNFUserRequestDto requestedByDto = new CNFUserRequestDto(
                inventoryRequest.getRequestedBy().getUserName(),
                inventoryRequest.getRequestedBy().getFullName(),
                inventoryRequest.getRequestedBy().getEmail()
        );
        return new InventoryItemRequestListOfCnfDTO(
                requestedByDto,
                inventoryRequest.getInventoryStatus(),
                detailDtos
        );
    }


    /**
     * Converts an InventoryRequestDetail entity to an InventoryRequestDetailDto.
     *
     * @param inventoryRequestDetail The inventory request detail entity to convert
     * @return InventoryRequestDetailDto representing the inventory request detail
     */
    private InventoryRequestDetailDto convertToInventoryRequestDetailDto(InventoryRequestDetail inventoryRequestDetail) {
        InventoryItem requestedItem = inventoryRequestDetail.getRequestedItem();
        InventoryItemDto requestedItemDto = new InventoryItemDto(requestedItem.getName(), requestedItem.getDescrition(), requestedItem.getHSNCode(), requestedItem.getExpiryDate(), requestedItem.getManufacturer(), requestedItem.getRegisterDate(), requestedItem.getUnitCost(), requestedItem.getSellingPrice(), requestedItem.getNoOfStock(), requestedItem.getStockKeepingUnit(), requestedItem.getInventoryCategory().getId(), requestedItem.getIsRefundable(), requestedItem.getIsExchangeable(), requestedItem.getBatchNumber(), requestedItem.getStorageCondition(), requestedItem.getTaxRate().getId());
        return new InventoryRequestDetailDto(inventoryRequestDetail.getQuantity(), requestedItemDto);
    }

    /**
     * Converts an InventoryRequest object to a CNFInventoryRequestDTO.
     *
     * @param inventoryRequest The InventoryRequest object to convert
     * @return CNFInventoryRequestDTO representing the converted InventoryRequest
     */
    private CNFInventoryRequestDTO convertToDTO(InventoryRequest inventoryRequest) {
        CNFInventoryRequestDTO dto = new CNFInventoryRequestDTO();
        dto.setInventoryStatus(inventoryRequest.getInventoryStatus());

        List<InventoryRequestDetail> details = inventoryRequest.getInventoryRequestDetail();
        if (details != null && !details.isEmpty()) {
            List<CNFInventoryRequestDetailDTO> detailDTOs = details.stream().map(this::getCnfInventoryRequestDetailDTO).collect(Collectors.toList());
            dto.setInventoryRequestDetail(detailDTOs);
        } else {
            dto.setInventoryRequestDetail(new ArrayList<>());
        }

        return dto;
    }

    /**
     * Converts an InventoryRequestDetail object to a CNFInventoryRequestDetailDTO.
     *
     * @param detail The InventoryRequestDetail object to convert
     * @return CNFInventoryRequestDetailDTO representing the converted InventoryRequestDetail
     */
    private CNFInventoryRequestDetailDTO getCnfInventoryRequestDetailDTO(InventoryRequestDetail detail) {
        CNFInventoryRequestDetailDTO detailDTO = new CNFInventoryRequestDetailDTO();
        detailDTO.setQuantity(detail.getQuantity());

        InventoryItem item = detail.getRequestedItem();
        if (item != null) {
            InventoryItemDTO itemDTO = new InventoryItemDTO();
            itemDTO.setName(item.getName());
            itemDTO.setDescrition(item.getDescrition());
            itemDTO.setExpiryDate(item.getExpiryDate());
            itemDTO.setRegisterDate(item.getRegisterDate());
            itemDTO.setBatchNumber(item.getBatchNumber());
            itemDTO.setStorageCondition(item.getStorageCondition());
            itemDTO.setUnitCost(item.getUnitCost());
            itemDTO.setSellingPrice(item.getSellingPrice());
            itemDTO.setNoOfStock(item.getNoOfStock());
            itemDTO.setStockKeepingUnit(item.getStockKeepingUnit());
            itemDTO.setIsRefundable(item.getIsRefundable());
            itemDTO.setIsExchangeable(item.getIsExchangeable());
            itemDTO.setHSNCode(item.getHSNCode());
            itemDTO.setManufacturer(item.getManufacturer());
            detailDTO.setInventoryItemDTO(itemDTO);
        }

        return detailDTO;
    }

    // Method to convert InventoryRequest to InventoryRequestDto
    public InventoryRequestResponseDTO convertToDtoInventoryRequest(User user, InventoryRequest request, List<InventoryRequestDetail> details) {
        InventoryRequestResponseDTO dto = new InventoryRequestResponseDTO();
        UserInventoryRequestDTO userDto = new UserInventoryRequestDTO();
        userDto.setFullname(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setUserName(user.getUserName());
        dto.setUserInventoryRequestDTO(userDto);
        dto.setInventoryStatus(request.getInventoryStatus());

        List<InventoryRequestResponseDetailDto> detailDtos = new ArrayList<>();
        for (InventoryRequestDetail detail : details) {
            InventoryRequestResponseDetailDto detailDto = convertToDto(detail);
            detailDtos.add(detailDto);
        }
        dto.setInventoryRequestDetail(detailDtos);
        return dto;
    }

    // Method to convert InventoryRequestDetail to InventoryRequestDetailDto
    public InventoryRequestResponseDetailDto convertToDto(InventoryRequestDetail detail) {
        InventoryRequestResponseDetailDto dto = new InventoryRequestResponseDetailDto();
        dto.setQuantity(detail.getQuantity());
//        dto.setTotalAmount(detail.getTotalAmount());
        InventoryItemDTO itemDto = new InventoryItemDTO();
        itemDto.setName(detail.getRequestedItem().getName());
        itemDto.setDescrition(detail.getRequestedItem().getDescrition());
        itemDto.setHSNCode(detail.getRequestedItem().getHSNCode());
        itemDto.setExpiryDate(detail.getRequestedItem().getExpiryDate());
        itemDto.setManufacturer(detail.getRequestedItem().getManufacturer());
        itemDto.setRegisterDate(detail.getRequestedItem().getRegisterDate());
        itemDto.setBatchNumber(detail.getRequestedItem().getBatchNumber());
        itemDto.setStorageCondition(detail.getRequestedItem().getStorageCondition());
        itemDto.setUnitCost(detail.getRequestedItem().getUnitCost());
        itemDto.setSellingPrice(detail.getRequestedItem().getSellingPrice());
        itemDto.setNoOfStock(detail.getRequestedItem().getNoOfStock());
        itemDto.setStockKeepingUnit(detail.getRequestedItem().getStockKeepingUnit());
        itemDto.setIsRefundable(detail.getRequestedItem().getIsRefundable());
        itemDto.setIsExchangeable(detail.getRequestedItem().getIsExchangeable());
        dto.setInventoryItemDTO(itemDto);
        return dto;
    }

    private CNFUserDTO convertToCNFUserDTO(User user) {
        CNFUserDTO dto = new CNFUserDTO();
        dto.setUserName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setDob(user.getDob());
        dto.setGender(user.getGender());
        return dto;
    }


}
