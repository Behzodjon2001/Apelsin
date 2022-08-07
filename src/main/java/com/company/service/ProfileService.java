package com.company.service;

import com.company.config.CustomUserDetails;
import com.company.dto.AuthDTO;
import com.company.dto.ProfileDTO;
import com.company.entity.ProfileEntity;
import com.company.enums.GeneralStatus;
import com.company.enums.GeneralRole;
import com.company.exception.BadRequestException;
import com.company.exception.ItemNotFoundException;
import com.company.repository.ProfileRepository;
import com.company.util.CurrentUser;
import com.company.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Slf4j
@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public ProfileDTO create(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByPhone(dto.getPhone());
        if (optional.isPresent()) {
            log.error("User already exists {}", dto);
            throw new BadRequestException("User already exists");
        }
//        GeneralRole role = checkRole(dto.getRole().name());

        ProfileEntity entity = new ProfileEntity();
        entity.setRole(GeneralRole.ROLE_USER);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setPassword(MD5Util.getMd5(dto.getPassword()));
        entity.setCreatedDate(LocalDateTime.now());
        entity.setStatus(GeneralStatus.ACTIVE);

        profileRepository.save(entity);
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setSurname(entity.getSurname());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public void nameAndSurnameUpdate(String pId, ProfileDTO dto) {

        Optional<ProfileEntity> profile = profileRepository.findById(pId);

        if (profile.isEmpty()) {
            log.error("Profile Not Found {}", dto);
            throw new ItemNotFoundException("Profile Not Found ");
        }


        ProfileEntity entity = profile.get();


        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setPassword(dto.getPassword());
        profileRepository.save(entity);
    }


    public ProfileEntity get(String id) {
        return profileRepository.findById(id).orElseThrow(() -> {
            log.error("Profile Not Found {}", id);
            throw new ItemNotFoundException("Profile not found");
        });
    }

    public ProfileEntity getProfile() {

        CustomUserDetails user = CurrentUser.getCurrentUser();
        return user.getProfile();
    }

    public ProfileDTO getProfile(String id) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        ProfileEntity entity = optional.get();
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setPhone(entity.getPhone());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setPassword(entity.getPassword());

        return dto;
    }

    private GeneralRole checkRole(String role) {
        try {
            return GeneralRole.valueOf(role);
        } catch (RuntimeException e) {
            log.error("Role is wrong {}", role);
            throw new BadRequestException("Role is wrong");
        }
    }

    public void delete(String id) {
        Optional<ProfileEntity> optional = profileRepository.checkDeleted(GeneralStatus.ACTIVE, id);
        if (optional.isEmpty()) {
            log.error("This user not found or already deleted! {}", id);
            throw new BadRequestException("This user not found or already deleted!");
        }
        profileRepository.changeStatus(GeneralStatus.NO_ACTIVE, id);
    }
}
