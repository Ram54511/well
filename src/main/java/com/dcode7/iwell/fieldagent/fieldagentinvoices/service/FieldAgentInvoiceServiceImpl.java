package com.dcode7.iwell.fieldagent.fieldagentinvoices.service;

import com.dcode7.iwell.common.exception.CustomException;
import com.dcode7.iwell.common.service.LocalService;
import com.dcode7.iwell.fieldagent.customer.Customer;
import com.dcode7.iwell.fieldagent.customer.CustomerRepository;
import com.dcode7.iwell.fieldagent.fieldagentinvoices.FieldAgentInvoice;
import com.dcode7.iwell.fieldagent.fieldagentinvoices.FieldAgentInvoiceDTO;
import com.dcode7.iwell.fieldagent.fieldagentinvoices.FieldAgentInvoiceRepository;
import com.dcode7.iwell.fieldagent.fieldagentinvoices.PaymentStatus;
import com.dcode7.iwell.fieldagent.fieldagentinvoices.fieldagentinvoiceitem.FieldAgentInvoiceItem;
import com.dcode7.iwell.fieldagent.fieldagentinvoices.fieldagentinvoiceitem.FieldAgentInvoiceItemDTO;
import com.dcode7.iwell.fieldagent.fieldagentinvoices.fieldagentinvoiceitem.FieldAgentInvoiceItemRepository;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItem;
import com.dcode7.iwell.user.inventory.inventoryitem.InventoryItemRepository;
import com.dcode7.iwell.user.inventory.inventorytransfer.InventoryStatus;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequest;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.InventoryRequestRepository;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetail;
import com.dcode7.iwell.user.inventory.inventorytransfer.inventoryrequest.inventoryrequestdetail.InventoryRequestDetailRepository;
import com.dcode7.iwell.utils.GenericUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FieldAgentInvoiceServiceImpl implements FieldAgentInvoiceService {

    private final CustomerRepository customerRepository;

    private final InventoryItemRepository inventoryItemRepository;

    private final InventoryRequestRepository inventoryRequestRepository;

    private final FieldAgentInvoiceRepository fieldAgentInvoiceRepository;

    private final FieldAgentInvoiceItemRepository fieldAgentInvoiceItemRepository;

    private final InventoryRequestDetailRepository inventoryRequestDetailRepository;

    private final LocalService localService;

    @Override
    /**
     * Creates a new FieldAgentInvoice based on the provided DTO.
     * Retrieves customer and inventory items, maps them to entities,
     * and saves the invoice along with its items.
     *
     * @param invoiceDTO The DTO containing invoice data.
     * @return The created FieldAgentInvoice entity.
     * @throws CustomException If customer or inventory items are not found.
     */
    @Transactional
    public FieldAgentInvoice createFieldAgentInvoice(FieldAgentInvoiceDTO invoiceDTO, PaymentStatus paymentStatus) {
        User user = GenericUtils.getLoggedInUser();
        Customer customer = getCustomerOrThrow(invoiceDTO.getCustomerId());
        FieldAgentInvoice invoice = new FieldAgentInvoice();
        invoice.setCustomer(customer);
        invoice.setPaymentStatus(paymentStatus);
        invoice.setPartialPayment(invoiceDTO.getPartialPayment());
        invoice.setCreditTerm(invoiceDTO.getCreditTerm());
        invoice = saveInvoice(invoice);
        List<FieldAgentInvoiceItem> invoiceItems = createInvoiceItems(invoiceDTO.getItems(), invoice);
        manipulateInventoryRequest(user, invoiceItems);
        calculateTotalAmountAndDiscount(invoice, invoiceItems);
        return saveInvoice(invoice);
    }

    private FieldAgentInvoice saveInvoice(FieldAgentInvoice invoice) {
        return fieldAgentInvoiceRepository.save(invoice);
    }


    /**
     * Manipulates the inventory request based on the given user and list of invoice items.
     * Retrieves the inventory request for the user with an approved status,
     * updates the inventory quantities based on the invoice items,
     * and saves the updated inventory request.
     *
     * @param user The user who requested the inventory.
     * @param invoiceItems The list of items in the invoice.
     * @return The updated InventoryRequest entity.
     * @throws CustomException If no approved inventory request is found for the user.
     */
    public InventoryRequest manipulateInventoryRequest(User user, List<FieldAgentInvoiceItem> invoiceItems) {
        InventoryRequest inventoryRequest = inventoryRequestRepository.getInventoryRequestByRequestedByAndInventoryStatus(user, InventoryStatus.APPROVED);
        if (inventoryRequest == null) {
            throw new CustomException(localService.getMessage("inventory.item.not.found"), HttpStatus.BAD_REQUEST);
        }
        List<InventoryRequestDetail> inventoryRequestDetails = inventoryRequest.getInventoryRequestDetail();
        for (FieldAgentInvoiceItem item : invoiceItems) {
            for (InventoryRequestDetail detail : inventoryRequestDetails) {
                if (item.getInventoryItem().equals(detail.getRequestedItem())) {
                    Integer inventoryQuantity = detail.getQuantity();
                    Integer invoiceQuantity = item.getQuantity();
                    Integer updatedQuantity = inventoryQuantity - invoiceQuantity;

                    if (updatedQuantity <= 0) {
                        String errorMessage = String.format(
                                localService.getMessage("inventory.quantity.insufficient"),
                                item.getInventoryItem().getName(),
                                inventoryQuantity,
                                invoiceQuantity,
                                -updatedQuantity
                        );
                        throw new CustomException(errorMessage, HttpStatus.BAD_REQUEST);
                    }
                    detail.setQuantity(updatedQuantity);
                    inventoryRequestDetailRepository.save(detail);
                }
            }
        }
        return inventoryRequestRepository.save(inventoryRequest);
    }


    /**
     * Retrieves a Customer entity by its ID or throws CustomException if not found.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return The Customer entity.
     * @throws CustomException If customer with the given ID is not found.
     */
    private Customer getCustomerOrThrow(UUID customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(localService.getMessage("customer.not.found"), HttpStatus.BAD_REQUEST));
    }

    /**
     * Maps FieldAgentInvoiceDTO to FieldAgentInvoice entity.
     *
     * @param invoiceDTO The DTO containing invoice data.
     * @param customer   The Customer entity associated with the invoice.
     * @return The mapped FieldAgentInvoice entity.
     */
    private FieldAgentInvoice mapToEntity(FieldAgentInvoiceDTO invoiceDTO, Customer customer,PaymentStatus paymentStatus) {
        FieldAgentInvoice invoice = new FieldAgentInvoice();
        invoice.setCustomer(customer);
        invoice.setPaymentStatus(paymentStatus);
        invoice.setPartialPayment(invoiceDTO.getPartialPayment());
        invoice.setCreditTerm(invoiceDTO.getCreditTerm());
        return invoice;
    }

    /**
     * Creates FieldAgentInvoiceItem entities based on the provided DTOs,
     * maps them to InventoryItem entities, and associates them with the invoice.
     *
     * @param itemDTOs The list of DTOs containing invoice item data.
     * @param invoice  The FieldAgentInvoice entity to which items belong.
     * @return The list of created FieldAgentInvoiceItem entities.
     * @throws CustomException If any inventory item with the given ID is not found.
     */
    private List<FieldAgentInvoiceItem> createInvoiceItems(List<FieldAgentInvoiceItemDTO> itemDTOs, FieldAgentInvoice invoice) {
        List<FieldAgentInvoiceItem> invoiceItems = new ArrayList<>();
        for (FieldAgentInvoiceItemDTO itemDTO : itemDTOs) {
            InventoryItem inventoryItem = getInventoryItemOrThrow(itemDTO.getInventoryItemId());
            FieldAgentInvoiceItem invoiceItem = new FieldAgentInvoiceItem();
            invoiceItem.setInventoryItem(inventoryItem);
            invoiceItem.setDealPrice(itemDTO.getDealPrice());
            invoiceItem.setQuantity(itemDTO.getQuantity());
            BigDecimal itemDiscount = inventoryItem.getSellingPrice().subtract(itemDTO.getDealPrice());
            invoiceItem.setDiscount(itemDiscount);
            invoiceItem.setFieldAgentInvoice(invoice);
            invoiceItem = saveInvoiceItem(invoiceItem);
            invoiceItems.add(invoiceItem);
        }
        return invoiceItems;
    }

    private FieldAgentInvoiceItem saveInvoiceItem(FieldAgentInvoiceItem invoiceItem) {
        return fieldAgentInvoiceItemRepository.save(invoiceItem);
    }

    /**
     * Retrieves an InventoryItem entity by its ID or throws CustomException if not found.
     *
     * @param inventoryItemId The ID of the inventory item to retrieve.
     * @return The InventoryItem entity.
     * @throws CustomException If inventory item with the given ID is not found.
     */
    private InventoryItem getInventoryItemOrThrow(UUID inventoryItemId) {
        return inventoryItemRepository.findById(inventoryItemId)
                .orElseThrow(() -> new CustomException(localService.getMessage("inventory.item.not.found"), HttpStatus.BAD_REQUEST));
    }


    /**
     * Calculates the total amount, total discount, and remaining amount for the invoice.
     * Updates the invoice entity with these calculated values.
     *
     * @param invoice The FieldAgentInvoice entity to update.
     * @param invoiceItems The list of FieldAgentInvoiceItem entities associated with the invoice.
     */
    private void calculateTotalAmountAndDiscount(FieldAgentInvoice invoice, List<FieldAgentInvoiceItem> invoiceItems) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (FieldAgentInvoiceItem item : invoiceItems) {
            BigDecimal itemTotalPrice = item.getDealPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotalPrice);
            BigDecimal itemDiscount = item.getInventoryItem().getSellingPrice().subtract(item.getDealPrice())
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            totalDiscount = totalDiscount.add(itemDiscount);
        }
        invoice.setTotalAmount(totalAmount);
        invoice.setDiscount(totalDiscount);
        BigDecimal partialPayment = invoice.getPartialPayment() != null ? invoice.getPartialPayment() : BigDecimal.ZERO;
        BigDecimal remainingAmount = totalAmount.subtract(partialPayment);
        invoice.setRemainingAmount(partialPayment.compareTo(BigDecimal.ZERO) > 0 ? remainingAmount : BigDecimal.ZERO);
    }
}
