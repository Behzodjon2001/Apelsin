package com.company.entity;

import com.company.entity.template.BaseEntity;
import com.company.enums.GeneralStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "merchant")
public class MerchantEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long cardNumber;

    @Column
    @Enumerated(EnumType.STRING)
    private GeneralStatus status= GeneralStatus.ACTIVE;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}
