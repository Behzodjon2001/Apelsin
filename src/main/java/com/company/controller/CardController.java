package com.company.controller;

import com.company.dto.CardDTO;
import com.company.entity.CardEntity;
import com.company.mapper.ResponseCard;
import com.company.service.CardService;
import com.company.util.CurrentUser;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Card CRUD for Bank")
@Slf4j
@RestController
@RequestMapping("/card")
public class CardController {
    @Autowired
    private CardService cardService;

    @PostMapping("/user/create")
    public ResponseEntity<?> create(@RequestBody CardDTO dto) {
        log.info("Request for create {}" , dto);
        ResponseCard profileDTO = cardService.create(dto);
        return ResponseEntity.ok().body(profileDTO);
    }
//
//    @PutMapping("/user/update/{cardId}")
//    public ResponseEntity<?> phoneUpdate(@PathVariable String cardId, @RequestBody CardDTO dto) {
//        log.info("Request for update {}" , dto);
//        cardService.update(cardId, dto);
//        return ResponseEntity.ok().body("Successfully updated");
//    }
//
    @PutMapping("/user/statusUpdate/{number}")
    public ResponseEntity<?> statusUpdate(@PathVariable String number,@RequestBody CardDTO dto) {
        log.info("Request for update {}" , dto);
        cardService.statusUpdate(number, dto);
        return ResponseEntity.ok().body("Successfully updated");
    }


    @GetMapping("/user/getCardId")
    public ResponseEntity<?> getCardById() {
        log.info("Request for list {}" );
        List<CardDTO> list = cardService.getCardById(CurrentUser.getCurrentUser().getProfile().getId());
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/user/getNameSurnameCardId/{number}")
    public ResponseEntity<?> getNameSurnameCardId(@PathVariable String number) {
        log.info("Request for list {}", number );
        CardDTO list = cardService.getNameSurnameCardId();
        return ResponseEntity.ok().body(list);
    }
}
