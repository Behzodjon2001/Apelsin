package com.company.entity;

import com.company.entity.template.BaseEntity;
import com.company.enums.GeneralStatus;
import com.company.enums.GeneralRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column
    private String surname;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private GeneralRole role;

    @Column
    @Enumerated(EnumType.STRING)
    private GeneralStatus status= GeneralStatus.ACTIVE;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
}
