package com.example.ggsb_back.domain.location.repository;

import com.example.ggsb_back.domain.location.entity.WaterLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaterLocationRepository extends JpaRepository<WaterLocation, Long> {
    @Query("select distinct w.CITY from WaterLocation w where w.STATE = ?1 order by w.CITY ASC")
    List<String> findCITYBySTATE(String state);

    @Query("select distinct w.DISTRICT from WaterLocation w where w.CITY = ?1 order by w.DISTRICT ASC")
    List<String> findDISTRICTByCITY(String city);

    Optional<WaterLocation> findByCITYAndDISTRICT(String city, String district);

    List<WaterLocation> findByWPID(Long WPID);
}