package com.example.ggsb_back.Controller;

import com.example.ggsb_back.DTO.Response.CityDTO;
import com.example.ggsb_back.DTO.Response.DistrictDTO;
import com.example.ggsb_back.Service.LocationService;
import com.example.ggsb_back.global.error.ErrorCode;
import com.example.ggsb_back.global.error.exception.InterServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private LocationService location_service;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        throw new InterServerException();
    }

    @GetMapping
    public ResponseEntity<?> retrieve_location(@RequestParam(value="state", required = false) String state, @RequestParam(value="city", required = false) String city) {

        if (state != null) {
            try {
                List<String> cities = location_service.retrieve_city();
                CityDTO response = new CityDTO();
                response.setState(state);
                response.setCity(cities);
                return ResponseEntity.ok().body(response);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("retrieve_city_error");
            }
        }
        else if (city != null) {
            try {
                List<String> districts = location_service.retrieve_district(city);
                DistrictDTO response = new DistrictDTO();
                response.setCity(city);
                response.setDistrict(districts);
                return ResponseEntity.ok().body(response);
            }
            catch (Exception e) {
                return ResponseEntity.badRequest().body("retrieve_district_error");
            }
        }
        else {
            return ResponseEntity.badRequest().body("retrieve_location_error");
        }
    }
}