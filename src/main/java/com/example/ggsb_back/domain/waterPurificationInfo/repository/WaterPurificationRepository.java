package com.example.ggsb_back.domain.waterPurificationInfo.repository;

import com.example.ggsb_back.domain.waterPurificationInfo.entity.WaterPurification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaterPurificationRepository extends JpaRepository<WaterPurification, Long> {
    Optional<WaterPurification> findByWPID(long wp_id);

    List<WaterPurification> findByTYPE(Integer type);
}
