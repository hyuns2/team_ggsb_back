package com.example.ggsb_back.domain.location.service;

import com.example.ggsb_back.domain.location.repository.WaterLocationRepository;
import com.example.ggsb_back.domain.location.dto.LocationDto;
import com.example.ggsb_back.global.error.exception.BadLocationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public LocationDto retrieveCity(String state) {
        List<String> list = locationRepository.findCITYBySTATE(state);
        if (list.isEmpty())
            throw new BadLocationException();
        return new LocationDto(state, list);
    }

    /**
     * 시에 속하는 모든 동네 반환
     *
     * @param city 검색할 시
     * @return DistrictDto 모든 동네 리스트 포함된 결과
     */
    public LocationDto retrieveDistrict(String city) {
        List<String> list = locationRepository.findDISTRICTByCITY(city);
        if (list.isEmpty())
            throw new BadLocationException();
        return new LocationDto(city, list);
    }
}