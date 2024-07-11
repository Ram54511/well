package com.dcode7.iwell.fieldagent.customer.service;

import com.dcode7.iwell.fieldagent.customer.Customer;
import com.dcode7.iwell.fieldagent.customer.CustomerDTO;

public interface CustomerService {
    Customer createCustomer(CustomerDTO customerDTO);
}
