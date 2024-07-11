package com.dcode7.iwell.CNF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInventoryRequestDTO {
    private String fullname;
    private String email;
    private String userName;
}
