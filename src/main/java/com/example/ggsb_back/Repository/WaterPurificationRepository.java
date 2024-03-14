package com.example.ggsb_back.Repository;

import com.example.ggsb_back.Entity.WaterPurification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaterPurificationRepository extends JpaRepository<WaterPurification, Long> {
    Optional<WaterPurification> findByWPID(long wp_id);

    List<WaterPurification> findByTYPE(Integer type);
}
