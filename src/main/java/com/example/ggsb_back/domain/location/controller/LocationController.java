package com.example.ggsb_back.domain.location.controller;

import com.example.ggsb_back.domain.location.service.LocationService;
import com.example.ggsb_back.domain.location.dto.CityDto;
import com.example.ggsb_back.domain.location.dto.DistrictDto;
import com.example.ggsb_back.global.error.exception.BadLocationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<?> retrieveLocation(@RequestParam(value="state", required = false) String state, @RequestParam(value="city", required = false) String city) {
        if (state != null) {
            CityDto cityDto = locationService.retrieveCity(state);
            return new ResponseEntity<>(cityDto, HttpStatus.OK);
        }
        else if (city != null) {
            DistrictDto districtDto = locationService.retrieveDistrict(city);
            return new ResponseEntity<>(districtDto, HttpStatus.OK);
        }
        else
            throw new BadLocationException();
    }
}