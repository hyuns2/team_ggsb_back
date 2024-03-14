package com.example.ggsb_back.Service;


import com.example.ggsb_back.DTO.SearchRequestDTO;
import com.example.ggsb_back.DTO.WQuality;
import com.example.ggsb_back.global.elasticSearch.Indices;
import com.example.ggsb_back.Util.SearchUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class WaterPurificationInfoService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(WaterPurificationInfoService.class);

    private final RestHighLevelClient client;


    @Autowired
    public WaterPurificationInfoService(RestHighLevelClient client) {
        this.client = client;
    }



    public List<WQuality> search_TYPE0(final SearchRequestDTO dto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.WATERPURIFICATION_INDEX_0,dto
        );
        return searchInternal(request);
    }

    public List<WQuality> search(final SearchRequestDTO dto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.WATERPURIFICATION_INDEX,
                dto
        );
        return searchInternal(request);
    }

    public List<WQuality> search_TYPE1(final SearchRequestDTO dto) {
        final SearchRequest request = SearchUtil.buildSearchRequest(
                Indices.WATERPURIFICATION_INDEX_1,
                dto
        );
        return searchInternal(request);
    }

    private List<WQuality> searchInternal(final SearchRequest request) {
        if (request == null) {
            LOG.error("Failed to build search request");
            return Collections.emptyList();
        }

        try {
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            final SearchHit[] searchHits = response.getHits().getHits();
            final List<WQuality> WQualities = new ArrayList<>(searchHits.length);

            for (SearchHit hit : searchHits) {
                WQualities.add(
                        MAPPER.readValue(hit.getSourceAsString(), WQuality.class)
                );
            }

            return WQualities;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public WQuality getByFcltyMngNm(final String FcltyMngNm) {
        try {

            final GetResponse documentFields = client.get(
                    new GetRequest(Indices.WATERPURIFICATION_INDEX_0, FcltyMngNm),
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


}
