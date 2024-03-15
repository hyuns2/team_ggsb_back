package com.example.ggsb_back.domain.location;

import com.example.ggsb_back.domain.location.entity.WaterLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaterLocationRepository extends JpaRepository<WaterLocation, Long> {
    List<String> findCITYBySTATE(String state);

    List<String> findDistinctByCITY(String city);

    Optional<WaterLocation> findByCITYAndDISTRICT(String city, String district);

    List<WaterLocation> findByWPID(Long WPID);
}