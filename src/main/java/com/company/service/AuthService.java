package com.company.service;

import com.company.config.CustomUserDetails;
import com.company.dto.AuthDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.ResponseInfoDTO;
import com.company.dto.VerificationDTO;
import com.company.entity.ProfileEntity;
import com.company.entity.SmsEntity;
import com.company.enums.GeneralRole;
import com.company.enums.GeneralStatus;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ProfileRepository;
import com.company.repository.SmsRepository;
import com.company.util.JwtUtil;
import com.company.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SmsRepository smsRepository;
    @Autowired
    private SmsService smsService;

    public String registration(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByPhone(dto.getPhone());
        if (optional.isPresent()) {
            log.error("This user already exist {}" , dto);
            throw new BadRequestException("This user already exist");
        }

        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setPhone(dto.getPhone());
        entity.setRole(GeneralRole.ROLE_USER);
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setStatus(GeneralStatus.NO_ACTIVE);

        profileRepository.save(entity);

        String s = smsService.sendRegistrationSms(entity.getPhone());

        return "Your registration code => " + s;
    }

    public ProfileDTO login(AuthDTO authDTO) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authDTO.getPhone(), authDTO.getPassword()));
        CustomUserDetails user = (CustomUserDetails) authenticate.getPrincipal();
        ProfileEntity profile = user.getProfile();

        ProfileDTO dto = new ProfileDTO();
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setJwt(JwtUtil.encode(profile.getId(), profile.getRole()));

        return dto;
    }

    public ProfileEntity get(String id) {
        return profileRepository.findById(id).orElseThrow(() -> {
            log.error("Profile Not Found {}", id);
            throw new ItemNotFoundException("Profile not found");
        });
    }

    public String verification(VerificationDTO dto) {
        Optional<SmsEntity> optional = smsRepository.findTopByPhoneOrderByCreatedDateDesc(dto.getPhone());
        if (optional.isEmpty()) {
            return "Phone Not Found";
        }

        SmsEntity sms = optional.get();
        LocalDateTime validDate = sms.getCreatedDate().plusMinutes(1);

        if (validDate.isBefore(LocalDateTime.now())) {
            return "Time is out";
        }

        profileRepository.updateStatusByPhone(dto.getPhone(), GeneralStatus.ACTIVE);
        return "Verification Done";
    }

    public ResponseInfoDTO resendSms(String phone) {
        Long count = smsService.getSmsCount(phone);
        if (count >= 4) {
            return new ResponseInfoDTO(-1, "Limit dan o'tib getgan");
        }

        String s = smsService.sendRegistrationSms(phone);
        return new ResponseInfoDTO(1,s);
    }
}
