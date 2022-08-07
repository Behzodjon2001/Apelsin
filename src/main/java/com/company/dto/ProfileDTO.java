package com.company.dto;

import com.company.enums.GeneralStatus;
import com.company.enums.GeneralRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private String id;

    private String name;

    private String surname;

    private String phone;

    private String password;

    private GeneralStatus status;

    private LocalDateTime createdDate;

    private GeneralRole role;

    private String jwt;
}
