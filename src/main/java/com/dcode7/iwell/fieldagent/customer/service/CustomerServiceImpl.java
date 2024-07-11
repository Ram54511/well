package com.dcode7.iwell.fieldagent.customer.service;

import com.dcode7.iwell.fieldagent.customer.Customer;
import com.dcode7.iwell.fieldagent.customer.CustomerDTO;
import com.dcode7.iwell.fieldagent.customer.CustomerRepository;
import com.dcode7.iwell.user.User;
import com.dcode7.iwell.utils.GenericUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(CustomerDTO customerDTO) {
        User user = GenericUtils.getLoggedInUser();
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setAddress(customerDTO.getAddress());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setFieldAgent(user);
        return customerRepository.save(customer);
    }
}
