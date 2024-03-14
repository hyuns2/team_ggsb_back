package com.example.ggsb_back.Controller;

import com.example.ggsb_back.DTO.Response.WGraphDTO;

import com.example.ggsb_back.Service.WaterQualityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/waterquality")
@Slf4j
public class WaterQualityController {
    private final WaterQualityService waterQualityService;

    @Autowired
    public WaterQualityController(WaterQualityService waterQualityService){
        this.waterQualityService = waterQualityService;
    }
    @GetMapping("/graph")
    public WGraphDTO searchWaterGraph(@RequestParam String city,
                                      @RequestParam String district,
                                      @RequestParam Integer range)
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
