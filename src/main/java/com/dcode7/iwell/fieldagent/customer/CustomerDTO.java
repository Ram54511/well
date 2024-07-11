package com.dcode7.iwell.fieldagent.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    @NotNull(message = "Customer name is required")
    private String name;

    @NotNull(message = "Customer email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Customer address is required")
    private String address;

    @NotNull(message = "Customer phone number is required")
    private String phoneNumber;

}
