package com.company.controller;

import com.company.dto.AuthDTO;
import com.company.dto.ProfileDTO;
import com.company.dto.ResponseInfoDTO;
import com.company.dto.VerificationDTO;
import com.company.service.AuthService;
import com.company.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Auth and registration")
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @ApiOperation(value = "registration", notes="Method for Authziration", nickname = "Some Nick Name")
    @PostMapping("/public/registration")
    public ResponseEntity<?> create(@RequestBody ProfileDTO dto) {
        log.info("Request for create {}" , dto);
        String profileDTO = authService.registration(dto);
        return ResponseEntity.ok().body(profileDTO);
    }

    @ApiOperation(value = "Login", notes="Method for Authziration", nickname = "Some Nick Name")
    @PostMapping("/userAdm/login")
    public ResponseEntity<ProfileDTO> login(@RequestBody AuthDTO dto) {
        log.info("Request for login {}" , dto);
        ProfileDTO generalDTO = authService.login(dto);
        return ResponseEntity.ok(generalDTO);
    }

    @ApiOperation(value = "Sms verifacation", notes="Method for Sms verifacation", nickname = "Some Nick Name")
    @PostMapping("/public/verification")
    public ResponseEntity<String> login(@RequestBody VerificationDTO dto) {
        log.info("Request for sms verifacation {}" , dto);
        String response = authService.verification(dto);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Resent Sms Code", notes="Method for Resent Sms")
    @GetMapping("/public/resend/{phone}")
    public ResponseEntity<ResponseInfoDTO> resendSms(@ApiParam(value = "phone", required = true, example = "998901234567")
                                                     @PathVariable("phone") String phone) {
        ResponseInfoDTO response = authService.resendSms(phone);
        return ResponseEntity.ok(response);
    }
}
