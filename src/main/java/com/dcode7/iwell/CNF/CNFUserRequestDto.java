package com.dcode7.iwell.CNF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CNFUserRequestDto {
    private String userName;
    private String fullName;
    private String email;
}
