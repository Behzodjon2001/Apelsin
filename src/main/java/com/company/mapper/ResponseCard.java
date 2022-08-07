package com.company.mapper;

import com.company.dto.CardDTO;
import lombok.Data;

@Data
public class ResponseCard {
    private String message;
    private CardDTO dto;

    public ResponseCard(String message, CardDTO dto) {
        this.message = message;
        this.dto = dto;
    }
}
