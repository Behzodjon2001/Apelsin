package com.company.service;

import com.company.config.CustomUserDetails;
import com.company.dto.AuthDTO;
import com.company.dto.CardDTO;
import com.company.dto.ProfileDTO;
import com.company.entity.CardEntity;
import com.company.entity.ProfileEntity;
import com.company.enums.GeneralStatus;
import com.company.exception.ItemNotFoundException;
import com.company.exception.NotAllowedException;
import com.company.mapper.ResponseCard;
import com.company.repository.CardRepository;
import com.company.repository.ProfileRepository;
import com.company.util.CurrentUser;
import com.company.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileRepository profileRepository;

    public ResponseCard create(CardDTO dto) {
        //Get Jwt Payment from username and password
        ProfileDTO profileDTO = getPaymentJwtToken();

        //Get Card info from payment Jwt
        ResponseEntity<CardDTO> response1 = getCardInfoFronJwt(dto, profileDTO);

        Optional<ProfileEntity> optional = profileRepository.findById(profileService.getProfile().getId());

        if (response1.getBody().getPhone().equals(optional.get().getPhone())) {

            CardEntity entity = new CardEntity();
            entity.setNumber(response1.getBody().getNumber());
            entity.setCreatedDate(response1.getBody().getCreatedDate());
            entity.setName("Uzcard");
            entity.setExpiredDate(response1.getBody().getExpiredDate());
            entity.setProfileId(profileService.getProfile().getId());

            cardRepository.save(entity);
            dto.setId(entity.getId());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setExpiredDate(entity.getExpiredDate());
            dto.setNumber(entity.getNumber());
            return new ResponseCard("Succussfully", dto);
        } else {
            return new ResponseCard("Not Join your phone number to card", null);
        }
    }

    //
//
//    public void update(String pId, CardDTO dto) {
//
//        Optional<CardEntity> profile = cardRepository.findById(pId);
//
//        if (profile.isEmpty()) {
//            log.error("Card Not Found {}", dto);
//            throw new ItemNotFoundException("Card Not Found ");
//        }
//
//        CardEntity entity = profile.get();
//        entity.setPhone(dto.getPhone());
//        cardRepository.save(entity);
//    }
//
    public void statusUpdate(String pId, CardDTO dto) {
        //Get Jwt Payment from username and password
        ProfileDTO profileDTO = getPaymentJwtToken();

        //Get Card info from payment Jwt

        ResponseEntity<CardDTO> response1 = getCardInfoFronJwt(dto, profileDTO);
        Optional<ProfileEntity> optional = profileRepository.findById(profileService.getProfile().getId());

        if (response1.getBody().getPhone().equals(optional.get().getPhone())) {

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();

            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + profileDTO.getJwt());


            HttpEntity<CardDTO> entity = new HttpEntity<CardDTO>(dto, headers);

            ResponseEntity<CardDTO> dto1 = restTemplate.exchange("http://localhost:8080/card/bankPayment/statusUpdate/" + dto.getNumber(), HttpMethod.PUT, entity, CardDTO.class);

            if (dto1.getBody().equals("Successfully updated")) {

                Optional<CardEntity> optional1 = cardRepository.findByNumber(pId);
                CardEntity card = optional1.get();
                if (card.getStatus().equals(GeneralStatus.ACTIVE)) {
                    card.setStatus(GeneralStatus.BLOCK);
                } else {
                    throw new NotAllowedException("Not Allowed");

                }
                cardRepository.save(card);
            } else {
                System.out.println("Your don't access");
            }

        }
    }


    public CustomUserDetails getProfile() {

        CustomUserDetails user = CurrentUser.getCurrentUser();
        return user;
    }

    public CardEntity get(String key) {
        return cardRepository.findById(key).orElseThrow(() -> {
            log.error("This Card not found {}", key);
            throw new ItemNotFoundException("This Card not found");
        });
    }


    public List<CardDTO> getCardById(String cardId) {
        Optional<ProfileEntity> cardIdEntity = profileRepository.findById(cardId);
        ProfileEntity entity = cardIdEntity.get();

        //Get Jwt Payment from username and password
        ProfileDTO profileDTO = getPaymentJwtToken();

        //Get Card info from payment Jwt

        RestTemplate restTemplate1 = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + profileDTO.getJwt());
        CardDTO dto1 = new CardDTO();
        dto1.setPhone(entity.getPhone());

        HttpEntity<CardDTO> cardDto = new HttpEntity<CardDTO>(dto1, headers);

        ResponseEntity<CardDTO[]> response1 = restTemplate1.exchange(
                "http://localhost:8080/card/payment/getCardPhone/" + dto1.getPhone(), HttpMethod.GET, cardDto, CardDTO[].class);

        for (int i = 0; i < response1.getBody().length; i++) {

            CardDTO dto = new CardDTO();
            dto.setId(response1.getBody()[i].getId());
            dto.setNumber(response1.getBody()[i].getNumber());
            dto.setCreatedDate(response1.getBody()[i].getCreatedDate());
            dto.setExpiredDate(response1.getBody()[i].getExpiredDate());
            dto.setPhone(entity.getPhone());
            List<CardDTO> list = new ArrayList<>();
            list.add(dto);
            return list;
        }
        return null;

    }

//    public CardDTO getNameSurnameCardId(String number){
//        //Get Jwt Payment from username and password
//        ProfileDTO profileDTO = getPaymentJwtToken();
//
//        //Get Card info from payment Jwt
//        ResponseEntity<CardDTO> response1 = getCardInfoFronJwt(dto, profileDTO);
//
//    }

    public ProfileDTO getPaymentJwtToken() {
        //Get Jwt Payment from username and password
        RestTemplate restTemplate = new RestTemplate();

        AuthDTO authDTO = new AuthDTO();
        authDTO.setUsername("apelsin");
        authDTO.setPassword("777");

        HttpEntity<AuthDTO> request = new HttpEntity<AuthDTO>(authDTO);

        ProfileDTO response = restTemplate.postForObject(
                "http://localhost:8080/auth/admBankPay/login", request, ProfileDTO.class);
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setJwt(JwtUtil.encode(response.getId(), response.getRole()));

        return profileDTO;
    }

    public ResponseEntity<CardDTO> getCardInfoFronJwt(CardDTO dto, ProfileDTO profileDTO) {
        RestTemplate restTemplate1 = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + profileDTO.getJwt());


        HttpEntity<CardDTO> cardDto = new HttpEntity<CardDTO>(dto, headers);

        ResponseEntity<CardDTO> response1 = restTemplate1.exchange(
                "http://localhost:8080/card/payment/getCardNumber/" + dto.getNumber(), HttpMethod.GET, cardDto, CardDTO.class);

        return response1;
    }
//    public ResponseEntity<CardDTO> getCardInfoFronJwt(CardDTO dto, ProfileDTO profileDTO) {
//        RestTemplate restTemplate1 = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.set("Authorization", "Bearer " + profileDTO.getJwt());
//
//
//        HttpEntity<CardDTO> cardDto = new HttpEntity<CardDTO>( headers);
//
//        ResponseEntity<CardDTO> response1 = restTemplate1.exchange(
//                "http://localhost:8080/card/payment/getCardNumber/" + dto.getNumber(), HttpMethod.GET, cardDto, CardDTO.class);
//
//        return response1;
//    }
}
