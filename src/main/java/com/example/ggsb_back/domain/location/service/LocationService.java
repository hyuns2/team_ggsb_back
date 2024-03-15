package com.example.ggsb_back.domain.location.service;

import com.example.ggsb_back.domain.location.repository.WaterLocationRepository;
import com.example.ggsb_back.domain.location.dto.CityDto;
import com.example.ggsb_back.domain.location.dto.DistrictDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocationService {
    private final WaterLocationRepository locationRepository;

    /**
     * 도에 속하는 모든 도시 반환
     *
     * @param state 검색할 도
     * @return CityDto 모든 도시 리스트 포함된 결과
     */
    public CityDto retrieveCity(String state) {
        CityDto dto = new CityDto();
        dto.setState(state);
        dto.setCity(locationRepository.findCITYBySTATE(state));
        return dto;
    }

    /**
     * 시에 속하는 모든 동네 반환
     *
     * @param city 검색할 시
     * @return DistrictDto 모든 동네 리스트 포함된 결과
     */
    public DistrictDto retrieveDistrict(String city) {
        DistrictDto dto = new DistrictDto();
        dto.setCity(city);
        dto.setDistrict(locationRepository.findDistinctByCITY(city));
        return dto;
    }
}