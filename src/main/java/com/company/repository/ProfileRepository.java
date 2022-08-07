package com.company.repository;

import com.company.entity.ProfileEntity;
import com.company.enums.GeneralStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, String> {
    Optional<ProfileEntity> findByPhone(String phone);

    @Query("from ProfileEntity  where status = ?1 and id = ?2")
    Optional<ProfileEntity> checkDeleted(GeneralStatus status, String id);

    @Transactional
    @Modifying
    @Query("update ProfileEntity set status = ?1 where id = ?2")
    void changeStatus(GeneralStatus status, String id);

    @Modifying
    @Transactional
    @Query("update  ProfileEntity p set p.status=?2 where p.phone=?1")
    void updateStatusByPhone(String phone, GeneralStatus status);
}
