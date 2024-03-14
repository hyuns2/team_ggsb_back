package com.example.ggsb_back.Repository;

import com.example.ggsb_back.Entity.WaterLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaterLocationRepository extends JpaRepository<WaterLocation, Long> {

    @Query("select distinct t.CITY from WaterLocation t order by t.CITY")
    List<String> find_city();

    @Query("select distinct h.DISTRICT from WaterLocation h where h.CITY = ?1 order by h.DISTRICT")
    List<String> find_district(String city);

    Optional<WaterLocation> findByCITYAndDISTRICT(String city, String district);

    List<WaterLocation> findByWPID(Long WPID);
}