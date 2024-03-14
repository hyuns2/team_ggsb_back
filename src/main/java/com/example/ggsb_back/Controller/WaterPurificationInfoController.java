package com.example.ggsb_back.Controller;


import com.example.ggsb_back.DTO.SearchRequestDTO;
import com.example.ggsb_back.DTO.WQuality;
import com.example.ggsb_back.Service.WaterPurificationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/waterquality")
public class WaterPurificationInfoController {
    private final WaterPurificationInfoService service;

    @Autowired
    public WaterPurificationInfoController(WaterPurificationInfoService service) {
        this.service = service;
    }

    @GetMapping("/{w_name}")
    public WQuality getByFcltyMngNm(@PathVariable final String w_name) {
        return service.getByFcltyMngNm(w_name);
    }

    @PostMapping("/search")
    public List<WQuality> search(@RequestBody final SearchRequestDTO dto) {
        return service.search(dto);
    }
}
