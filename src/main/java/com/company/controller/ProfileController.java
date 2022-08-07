package com.company.controller;

import com.company.dto.ProfileDTO;
import com.company.service.ProfileService;
import com.company.util.CurrentUser;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api(tags = "Profile CRUD for Bank")
@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/admin/create")
    public ResponseEntity<?> create(@RequestBody ProfileDTO dto) {
        log.info("Request for create {}" , dto);
        ProfileDTO profileDTO = profileService.create(dto);
        return ResponseEntity.ok().body(profileDTO);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> nameAndSurnameUpdate(@PathVariable("id") String id ,@RequestBody ProfileDTO dto) {
        log.info("Request for update {}" , dto);
        profileService.nameAndSurnameUpdate(id, dto);
        return ResponseEntity.ok().body("Successfully updated");
    }


    @GetMapping("/admin/getProfile/{id}")
    public ResponseEntity<?> list(@PathVariable("id") String id) {
        log.info("Request for list {}" , id);
        ProfileDTO dto = profileService.getProfile(id);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        log.info("Request for delete {}" , id);
        profileService.delete(id);
        return ResponseEntity.ok().body("Successfully deleted");
    }
}
