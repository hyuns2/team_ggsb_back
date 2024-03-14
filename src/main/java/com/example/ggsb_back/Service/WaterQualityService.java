package com.example.ggsb_back.Service;

import com.example.ggsb_back.DTO.Response.WALLInfoDTO;
import com.example.ggsb_back.DTO.Response.WGraphDTO;
import com.example.ggsb_back.DTO.Response.WInfoDTO;
import com.example.ggsb_back.DTO.Response.WPurificationDTO;
import com.example.ggsb_back.DTO.SearchRequestDTO;
import com.example.ggsb_back.DTO.WQuality;
import com.example.ggsb_back.global.elasticSearch.Indices;
import com.example.ggsb_back.domain.location.entity.WaterLocation;
import com.example.ggsb_back.Entity.WaterPurification;
import com.example.ggsb_back.domain.location.WaterLocationRepository;
import com.example.ggsb_back.Repository.WaterPurificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@AllArgsConstructor
public class WaterQualityService {
    //@Autowired
    private WaterLocationRepository locationRepository;
    //@Autowired
    private WaterPurificationRepository purificationRepository;
    private WaterPurificationInfoService waterPurificationInfoService;

    @Resource
    ElasticsearchOperations elasticsearchOperations;

    private static final Logger LOG = LoggerFactory.getLogger(WaterQualityService.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final RestHighLevelClient client;

    public WGraphDTO getWGraphDTO(final String city, final String district, final Integer range) {
        long wp_id = getWP_ID(city, district);
        WPurificationDTO w_dto = getWpDTO(wp_id);

        assert w_dto != null;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort(SortBuilders.fieldSort("datetime").order(SortOrder.ASC));
        searchSourceBuilder.size(1000);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("fcltyMngNm.keyword", w_dto.getWName()));

        Date to = new Date();
        Date from = new Date();

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

        List<Double> pHList = new ArrayList<>();
        List<Double> tbList = new ArrayList<>();
        List<Double> clList = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        int one_week = 7;
        int one_month = 28;
        String toDate = date.format(to);

        SearchRequest request;

        if (w_dto.getType() == 0) {
            request = new SearchRequest(Indices.WATERPURIFICATION_INDEX_0);
            //실시간데이터
            if (range == 0) {
                boolQueryBuilder.must(QueryBuilders.rangeQuery("datetime").from(toDate));
            } else if (range == 1) {
                from.setDate(to.getDate() - one_week);
                String fromDate = date.format(from);
                log.info("fromDate : {}", fromDate);
                boolQueryBuilder.must(QueryBuilders.rangeQuery("datetime").from(fromDate));
            } else {
                from.setDate(to.getDate() - one_month);
                String fromDate = date.format(from);
                boolQueryBuilder.must(QueryBuilders.rangeQuery("datetime").from(fromDate));
            }
        } else {
            //일일데이터
            //주간
            request = new SearchRequest(Indices.WATERPURIFICATION_INDEX_1);
            if (range == 1) {
                from.setDate(to.getDate() - one_week);
                String fromDate = date.format(from);
                log.info("fromDate : {}", fromDate);
                boolQueryBuilder.must(QueryBuilders.rangeQuery("datetime").from(fromDate));
            }
            //월간
            else if (range == 2) {
                from.setDate(to.getDate() - one_month);
                String fromDate = date.format(from);
                boolQueryBuilder.must(QueryBuilders.rangeQuery("datetime").from(fromDate));
            } else {
                log.error("range : {}", range);
                return null;
            }
        }
        return getValue(boolQueryBuilder, searchSourceBuilder, request, city, district, w_dto, pHList, tbList, clList, dates);
    }

    private WGraphDTO getValue(final BoolQueryBuilder boolQueryBuilder, final SearchSourceBuilder searchSourceBuilder, final SearchRequest request
            , String city, String district, WPurificationDTO w_dto, List<Double> pHList, List<Double> tbList, List<Double> clList, List<String> dates) {
        searchSourceBuilder.query(boolQueryBuilder);
        request.source(searchSourceBuilder);

        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();

            for (SearchHit hit : searchHits) {
                WQuality wQuality = MAPPER.readValue(hit.getSourceAsString(), WQuality.class);
                pHList.add(
                        checkValue(wQuality.getPhVal())
                );
                tbList.add(
                        checkValue(wQuality.getTbVal())
                );
                clList.add(
                        checkValue(wQuality.getClVal())
                );

                dates.add(
                        wQuality.getOccrrncDt()
                );
            }

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return WGraphDTO.builder()
                .city(city)
                .district(district)
                .waterPurification(w_dto)
                .dates(dates)
                .pHVals(pHList)
                .tbVals(tbList)
                .clVals(clList)
                .build();
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
            LOG.error(e.getMessage(), e);
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
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    private Double checkValue(String value) {
        Pattern pt1 = Pattern.compile("^[a-zA-Z]*$"); //영어 체크
        Pattern pt2 = Pattern.compile("^[가-힣]*$"); //한글 체크

        Matcher matcher1 = pt1.matcher(value);
        Matcher matcher2 = pt2.matcher(value);

        if (matcher1.find() || matcher2.find()) {
            value = "0";
            log.info("value: {}", value);
        }
        log.info("double.value : {}", Double.parseDouble(value));
        return Double.parseDouble(value);
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
