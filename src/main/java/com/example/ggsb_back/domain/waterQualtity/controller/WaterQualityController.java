package com.example.ggsb_back.domain.waterQualtity.controller;

import com.example.ggsb_back.domain.waterQualtity.dto.WGraphDTO;

import com.example.ggsb_back.domain.waterQualtity.service.WaterQualityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/waterquality")
public class WaterQualityController {
    private final WaterQualityService waterQualityService;
    
    @ResponseBody
    @GetMapping("/graph")
    public WGraphDTO searchWaterGraph(@RequestParam String city, @RequestParam String district, @RequestParam Integer range)
    {
        return waterQualityService.getWGraphDTO(city, district, range);
    }

    @GetMapping()
    public ResponseEntity<?> findAllWaterQuality(@RequestParam(required = false) String city,
                                                 @RequestParam(required = false) String district,
                                                 @RequestParam(required = false) String today,
                                                 @RequestParam(required = false) String hour){
        try {
            if(city==null && district==null) {
                log.info("다건조회");
                return ResponseEntity.ok().body(waterQualityService.findWaterQuality(today, hour));
            }
            else {
                log.info("단건조회");
                return ResponseEntity.ok().body(waterQualityService.getInfoDTO(city, district, today, hour));
            }
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
