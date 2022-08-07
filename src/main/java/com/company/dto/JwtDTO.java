package com.company.dto;

import com.company.enums.GeneralRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtDTO {
    private Integer id;
    private GeneralRole role;
}
