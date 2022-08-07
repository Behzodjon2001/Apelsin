package com.company.dto;

import com.company.enums.GeneralStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CardDTO {

    private String id;

    private String name;

    private Long number;

    private String phone;

    private Date expiredDate;

    private String profileId;

    private String profile;

    private LocalDateTime createdDate;

    private GeneralStatus status;
}
