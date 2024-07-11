package com.dcode7.iwell.CNF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CNFUserDTO {
    private String userName;
    private String fullName;
    private String email;
    private String mobileNumber;
    private Date dob;
    private Enum Gender;
}
