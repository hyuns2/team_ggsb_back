package com.example.ggsb_back.domain.location.controller;

import com.example.ggsb_back.domain.location.service.LocationService;
import com.example.ggsb_back.domain.location.dto.LocationDto;
;
import com.example.ggsb_back.global.error.exception.BadLocationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;
    
    @GetMapping
    public ResponseEntity<?> retrieveLocation(@RequestParam(value="state", required = false) String state, @RequestParam(value="city", required = false) String city) {
        LocationDto locationDto;
        if (state != null)
            locationDto = locationService.retrieveCity(state);
        else if (city != null)
            locationDto = locationService.retrieveDistrict(city);
        else
            throw new BadLocationException();
        return ResponseEntity.ok().body(locationDto);
    }
}