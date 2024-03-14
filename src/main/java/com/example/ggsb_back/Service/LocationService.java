package com.example.ggsb_back.Service;

import com.example.ggsb_back.Repository.WaterLocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LocationService {

    @Autowired
    private WaterLocationRepository location_repo;

    public List<String> retrieve_city() {
        return location_repo.find_city();
    }

    public List<String> retrieve_district(String city) {
        return location_repo.find_district(city);
    }
}