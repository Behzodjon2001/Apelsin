package com.company.entity;

import com.company.entity.template.BaseEntity;
import com.company.enums.TransactionType;
import com.company.enums.TransferStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transfer")
public class TransferEntity extends BaseEntity {

    @Column(name = "from_card_id")
    private String fromCardId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_card_id", updatable = false, insertable = false)
    private CardEntity fromCard;

    @Column(name = "to_card_id")
    private String toCardId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_card_id", updatable = false, insertable = false)
    private CardEntity toCard;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column
    private String uzcardTransferId;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionType type;
}
