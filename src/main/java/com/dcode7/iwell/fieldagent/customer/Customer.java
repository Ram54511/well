package com.dcode7.iwell.fieldagent.customer;

import com.dcode7.iwell.user.User;
import com.dcode7.iwell.utils.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull(message = "Customer name is required")
    private String name;

    @NotNull(message = "Customer email is required")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Customer address is required")
    private String address;

    @NotNull(message = "Customer phone number is required")
    @Column(unique = true)
    private String phoneNumber;

    @ManyToOne
    private User fieldAgent;
}
