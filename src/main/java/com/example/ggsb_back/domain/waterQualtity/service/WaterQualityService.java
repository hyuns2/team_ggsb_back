package com.example.ggsb_back.domain.waterQualtity.service;

import com.example.ggsb_back.domain.waterQualtity.dto.WALLInfoDTO;
import com.example.ggsb_back.domain.waterQualtity.dto.WGraphDTO;
import com.example.ggsb_back.domain.waterQualtity.dto.WInfoDTO;
import com.example.ggsb_back.domain.waterQualtity.dto.WPurificationDTO;
import com.example.ggsb_back.domain.waterPurificationInfo.dto.SearchRequestDTO;
import com.example.ggsb_back.domain.waterQualtity.entity.WQuality;
import com.example.ggsb_back.domain.waterPurificationInfo.service.WaterPurificationInfoService;
import com.example.ggsb_back.global.elasticSearch.Indices;
import com.example.ggsb_back.domain.location.entity.WaterLocation;
import com.example.ggsb_back.domain.waterPurificationInfo.entity.WaterPurification;
import com.example.ggsb_back.domain.location.repository.WaterLocationRepository;
import com.example.ggsb_back.domain.waterPurificationInfo.repository.WaterPurificationRepository;
import com.example.ggsb_back.global.error.exception.BadLocationException;
import com.example.ggsb_back.global.error.exception.BadPurificationException;
import com.example.ggsb_back.global.error.exception.ElasticSearchException;
import com.example.ggsb_back.global.util.DateUtil;
import com.example.ggsb_back.global.util.SearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
@Service
public class WaterQualityService {
    private final WaterLocationRepository locationRepository;
    private final WaterPurificationRepository purificationRepository;
    private final WaterPurificationInfoService waterPurificationInfoService;
    private final RestHighLevelClient client;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 그래프화하기 위해 연속적인 데이터 반환
     *
     * @param city 도시
     * @param district 동네
     * @param range 0:일간/1:주간/2:월간 검색
     *
     * @return WGraphDTO 그래프 정보
     */
    public WGraphDTO getWGraphDTO(final String city, final String district, final Integer range) {
        WaterLocation location = getWL(city, district);
        WaterPurification purification = getWp(location.getWPID());

        List<Double> pHList = new ArrayList<>();
        List<Double> tbList = new ArrayList<>();
        List<Double> clList = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        Date to = new Date();
        Date from = new Date();
        int one_week = 7;
        int one_month = 28;
        if (range == 0)
            from.setDate(to.getDate());
        else if (range == 1)
            from.setDate(to.getDate() - one_week);
        else
            from.setDate(to.getDate() - one_month);
        String fromDate = DateUtil.DateToString(from);

        String index;
        if (purification.getTYPE() == 0)
            index = Indices.WATERPURIFICATION_INDEX_0;
        else
            index = Indices.WATERPURIFICATION_INDEX_1;

        SearchRequest request = SearchUtil.buildSearchRequestByDate(index, purification.getWPNAME(), fromDate);
        getValue(request, pHList, tbList, clList, dates);
        return WGraphDTO.builder()
                .city(city)
                .district(district)
                .waterPurification(new WPurificationDTO(purification.getWPNAME(), purification.getTYPE()))
                .dates(dates)
                .pHVals(pHList)
                .tbVals(tbList)
                .clVals(clList)
                .build();
    }

    private WaterLocation getWL(String city, String district) {
        Optional<WaterLocation> result = locationRepository.findByCITYAndDISTRICT(city, district);

        if (result.isEmpty())
            throw new BadLocationException();

        return result.get();
    }

    private WaterPurification getWp(long wpId) {
        Optional<WaterPurification> result = purificationRepository.findByWPID(wpId);

        if (result.isEmpty())
            throw new BadPurificationException();

        return result.get();
    }

    private Double checkValue(String value) {
        Pattern pt1 = Pattern.compile("^[a-zA-Z]*$"); //영어 체크
        Pattern pt2 = Pattern.compile("^[가-힣]*$"); //한글 체크

        Matcher matcher1 = pt1.matcher(value);
        Matcher matcher2 = pt2.matcher(value);

        if (matcher1.find() || matcher2.find())
            value = "0";

        return Double.parseDouble(value);
    }

    private void getValue(final SearchRequest request, List<Double> pHList, List<Double> tbList, List<Double> clList, List<String> dates) {
        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();

            for (SearchHit hit : searchHits) {
                WQuality wQuality = MAPPER.readValue(hit.getSourceAsString(), WQuality.class);
                pHList.add(checkValue(wQuality.getPhVal()));
                tbList.add(checkValue(wQuality.getTbVal()));
                clList.add(checkValue(wQuality.getClVal()));
                dates.add(wQuality.getOccrrncDt());
            }
        } catch (Exception e) {
            throw new ElasticSearchException();
        }
    }

    private WQuality getWQuality_0(final String id) {
        try {
            final GetResponse documentFields = client.get(
                    new GetRequest(Indices.WATERPURIFICATION_INDEX_0, id),
                    RequestOptions.DEFAULT
            );
            if (documentFields == null || documentFields.isSourceEmpty()) {
                return null;
            }

            return MAPPER.readValue(documentFields.getSourceAsString(), WQuality.class);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private WQuality getWQuality_1(final String id) {
        try {
            final GetResponse documentFields = client.get(
                    new GetRequest(Indices.WATERPURIFICATION_INDEX_1, id),
                    RequestOptions.DEFAULT
            );
            if (documentFields == null || documentFields.isSourceEmpty()) {
                return null;
            }

            return MAPPER.readValue(documentFields.getSourceAsString(), WQuality.class);
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private WInfoDTO wInfo(String city, String district, WPurificationDTO dto, WQuality qualityDTO) {
        return WInfoDTO.builder()
                .city(city)
                .district(district)
                .waterPurification(dto)
                .pHVal(checkValue(qualityDTO.getPhVal()))
                .tbVal(checkValue(qualityDTO.getTbVal()))
                .clVal(checkValue(qualityDTO.getClVal()))
                .build();
    }

    private WInfoDTO noWinfo(String city, String district, WPurificationDTO dto) {
        return WInfoDTO.builder()
                .city(city)
                .district(district)
                .waterPurification(dto)
                .pHVal(0.0)
                .tbVal(0.0)
                .clVal(0.0)
                .build();
    }

    public WInfoDTO getInfoDTO(String city, String district, String today, String hour) {
        long wp_id = getWP_ID(city, district);
        WPurificationDTO dto = getWpDTO(wp_id);

        //실시간 데이터
        assert dto != null;
        if (dto.getType() == 0) {
            String replace_today = today.replaceAll("-", "");
            String today_hour = replace_today + hour;

            String id_0 = today_hour + dto.getWName();
            log.info("id : {}", id_0);

            WQuality qualityDTO = getWQuality_0(id_0);

            if (qualityDTO == null) {
                log.error("No data");
                return noWinfo(city, district, dto);
            } else {
                return wInfo(city, district, dto, qualityDTO);
            }
        }
        //일일데이터
        else if (dto.getType() == 1) {
            String Today = today.replaceAll("-", "");
            String id_1 = Today + dto.getWName();
            log.info("id : {}", id_1);

            WQuality qualityDTO = getWQuality_1(id_1);
            //데이터가 없을 때
            if (qualityDTO == null) {
                log.error("No data");
                return noWinfo(city, district, dto);
            } else {
                return wInfo(city, district, dto, qualityDTO);
            }
        } else {
            return noWinfo(city, district, dto);
        }
    }

    private long getWP_ID(String city, String district) {
        Optional<WaterLocation> l_entity = locationRepository.findByCITYAndDISTRICT(city, district);
        if (l_entity.isPresent()) {
            WaterLocation w_loc = l_entity.get();
            long wp_id = w_loc.getWPID();
            log.info("wp_id : {}", wp_id);

            return wp_id;
        } else return 0;

    }

    private WPurificationDTO getWpDTO(long wp_id) {
        Optional<WaterPurification> p_entity = purificationRepository.findByWPID(wp_id);
        if (p_entity.isPresent()) {
            WaterPurification w_pur = p_entity.get();

            return WPurificationDTO.builder()
                    .wName(w_pur.getWPNAME())
                    .type(w_pur.getTYPE())
                    .build();
        } else return null;
    }

    public List<WALLInfoDTO> findWaterQuality(String today, String hour) {
        String Today = today.replaceAll("-", "");
        String Today_Hour = Today + hour;

        log.info("Todayhour : {}", Today_Hour);

        List<String> field = new ArrayList<>();
        field.add("occrrncDt");
        List<WaterPurification> Type0_waterPurifications = purificationRepository.findByTYPE(0);
        List<WaterPurification> Type1_waterPurifications = purificationRepository.findByTYPE(1);

        SearchRequestDTO searchRequestDTO_TYPE0 = SearchRequestDTO.builder().size(100).fields(field).searchTerm(Today_Hour).build();
        SearchRequestDTO searchRequestDTO_TYPE1 = SearchRequestDTO.builder().size(100).fields(field).searchTerm(Today).build();

        List<WQuality> wQualities_TYPE0 = waterPurificationInfoService.search_TYPE0(searchRequestDTO_TYPE0);
        List<WQuality> wQualities_TYPE1 = waterPurificationInfoService.search_TYPE1(searchRequestDTO_TYPE1);

        List<WALLInfoDTO> WInfoDTOs = new ArrayList<>();

        for (int i = 0; i < Type0_waterPurifications.size(); i++) {
            List<WaterLocation> locations = locationRepository.findByWPID(Type0_waterPurifications.get(i).getWPID());
            for (int j = 0; j < locations.size(); j++) {
                for (int k = 0; k < wQualities_TYPE0.size(); k++) {
                    if (Type0_waterPurifications.get(i).getWPNAME().equals(wQualities_TYPE0.get(k).getFcltyMngNm())) {
                        WInfoDTOs.add(insertData(locations.get(j), wQualities_TYPE0.get(k)));
                    }
                }
            }
        }
        for (int i = 0; i < Type1_waterPurifications.size(); i++) {
            List<WaterLocation> locations = locationRepository.findByWPID(Type1_waterPurifications.get(i).getWPID());
            for (int j = 0; j < locations.size(); j++) {
                for (int k = 0; k < wQualities_TYPE1.size(); k++) {
                    if (Type1_waterPurifications.get(i).getWPNAME().equals(wQualities_TYPE1.get(k).getFcltyMngNm())) {
                        WInfoDTOs.add(insertData(locations.get(j), wQualities_TYPE1.get(k)));
                    }
                }
            }
        }
        return WInfoDTOs;
    }

    public WALLInfoDTO insertData(WaterLocation waterLocation, WQuality wQuality) {
        return WALLInfoDTO.builder()
                .city(waterLocation.getCITY())
                .district(waterLocation.getDISTRICT())
                .pHVal(checkValue(wQuality.getPhVal()))
                .clVal(checkValue(wQuality.getClVal()))
                .tbVal(checkValue(wQuality.getTbVal()))
                .build();
    }

}
