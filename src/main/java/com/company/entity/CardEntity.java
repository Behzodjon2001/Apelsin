package com.company.entity;

import com.company.entity.template.BaseEntity;
import com.company.enums.GeneralStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "card")
public class CardEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long number;

    @Column(name = "expired_date")
    private Date expiredDate;

    @Column(name = "profile_id")
    private String profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", updatable = false, insertable = false)
    private ProfileEntity profile;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column
    @Enumerated(EnumType.STRING)
    private GeneralStatus status=GeneralStatus.ACTIVE;
}
