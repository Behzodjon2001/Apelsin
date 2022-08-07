package com.company.repository;


import com.company.entity.SmsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SmsRepository extends JpaRepository<SmsEntity, String> {
    Optional<SmsEntity> findTopByPhoneOrderByCreatedDateDesc(String phone);

    @Query(value = "select count(*) from sms where phone =:phone and created_date > now() - INTERVAL '1 MINUTE' ",
            nativeQuery = true)
    Long getSmsCount(@Param("phone") String phone);
}
