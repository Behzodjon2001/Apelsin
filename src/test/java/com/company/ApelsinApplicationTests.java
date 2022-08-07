package com.company;

import com.company.config.CustomUserDetails;
import com.company.dto.ProfileDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.GeneralRole;
import com.company.enums.GeneralStatus;
import com.company.repository.ProfileRepository;
import com.company.util.JwtUtil;
import com.company.util.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootTest
class ApelsinApplicationTests {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void contextLoadsAdmin() {
        ProfileEntity entity = new ProfileEntity();
        entity.setRole(GeneralRole.valueOf("ROLE_ADMIN"));
        entity.setName("Behzodjon");
        entity.setSurname("Malikov");
        entity.setPhone("988914640908");
        entity.setPassword(MD5Util.getMd5("1255"));
        entity.setCreatedDate(LocalDateTime.now());
        entity.setStatus(GeneralStatus.ACTIVE);

        profileRepository.save(entity);
    }
    @Test
    void contextLoadsAdmind() {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken("988914640908", "1255"));
        CustomUserDetails user = (CustomUserDetails) authenticate.getPrincipal();
        ProfileEntity profile = user.getProfile();

        ProfileDTO dto = new ProfileDTO();
        dto.setName(profile.getName());
        dto.setSurname(profile.getSurname());
        dto.setJwt(JwtUtil.encode(profile.getId(), profile.getRole()));
        System.out.println(dto);
    }

    @Test
    void contextLoads() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://10.10.3.140:8080/api/v1/company/pagination", String.class);
        System.out.println(response);
    }

    @Test
    void contextLoadsArray() {
        RestTemplate restTemplate = new RestTemplate();
        ProfileDTO[] response = restTemplate.getForObject("http://10.10.3.140:8080/api/v1/company/pagination", ProfileDTO[].class);
        System.out.println(Arrays.toString(response));
    }

    @Test
    void contextLoadsPathVeriable() {
        RestTemplate restTemplate = new RestTemplate();
        String id = "12asd";
        ProfileDTO response = restTemplate.getForObject("http://10.10.3.140:8080/api/v1/company/" + "2c950081823571320182357151900000", ProfileDTO.class);
        System.out.println(response);
    }

    @Test
    void contextLoadsRequestParam() {
        RestTemplate restTemplate = new RestTemplate();
        int s = 10;
        int p = 0;
        ProfileDTO[] response = restTemplate.getForObject("http://10.10.3.140:8080/api/v1/company/pagination?size={s}&page={p}", ProfileDTO[].class, s, p);
        System.out.println(Arrays.toString(response));

    }

    @Test
    void contextLoadsPathVeriableREsponse() {
        RestTemplate restTemplate = new RestTemplate();
        String id = "12asd";
        ResponseEntity<ProfileDTO> response = restTemplate.getForEntity("http://10.10.3.140:8080/api/v1/company/" + "2c950081823571320182357151900000", ProfileDTO.class);
        System.out.println(response.getBody());
        System.out.println(response.getStatusCode());
        System.out.println(response.getHeaders());
        System.out.println(response);
    }

    @Test
    void get4() {
        RestTemplate restTemplate = new RestTemplate();

        ProfileDTO companyDTO = new ProfileDTO();
        companyDTO.setName("nimadir ");
//        companyDTO.setContact("dasda");
//        companyDTO.setAddress("dasda");
//        companyDTO.setRole("PAYMENT");
//        companyDTO.setUsername("Company 5");
        companyDTO.setPassword("asdasd");

        HttpEntity<ProfileDTO> request = new HttpEntity<ProfileDTO>(companyDTO);

        Void response = restTemplate.postForObject(
                "http://localhost:8080/api/v1/company/create", request, Void.class);

        System.out.println(response);
    }

    @Test
    void get5() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer dasdasdhasdvasvdajsdjagdjasdjgashgd");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        Void response = restTemplate.postForObject(
                "http://localhost:8080/api/v1/company/create", entity, Void.class);

        System.out.println(response);
    }

    @Test
    void get7() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

//        CompanyDTO companyDTO = new CompanyDTO();
//        companyDTO.setName("nimadir ");
//        companyDTO.setContact("dasda");
//        companyDTO.setAddress("dasda");
//        companyDTO.setRole("PAYMENT");
//        companyDTO.setUsername("Company 5");
//        companyDTO.setPassword("asdasd");

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer dasdasdhasdvasvdajsdjagdjasdjgashgd");


//        HttpEntity<CompanyDTO> entity = new HttpEntity<CompanyDTO>(companyDTO, headers);

//        restTemplate.put(
//                "http://localhost:8080/api/v1/company/create", entity);
    }

    @Test
    void get8() {
        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<CompanyDTO> response = restTemplate.exchange("http://localhost:8080/api/v1/company/2c950081823571320182357151900000",
//                HttpMethod.GET, null, CompanyDTO.class);
//        System.out.println(response.getBody());
    }

}
