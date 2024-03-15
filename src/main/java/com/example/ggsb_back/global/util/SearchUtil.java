package com.example.ggsb_back.global.util;

import com.example.ggsb_back.domain.waterPurificationInfo.dto.SearchRequestDTO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.common.util.CollectionUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.List;

public final class SearchUtil {
    private static int DEFAULT_PAGE = 0;
    private static int DEFAULT_SIZE = 1000;

    public static SearchRequest buildSearchRequestByDate(final String indexName, final String purification, final String date) {
        int page = DEFAULT_PAGE;
        int size = DEFAULT_SIZE;
        int from = page <= 0 ? 0 : page * size;

        SearchSourceBuilder builder = new SearchSourceBuilder()
                .from(from)
                .size(size)
                .sort("datetime", SortOrder.ASC)
                .postFilter(getBoolQueryBuilder(purification, date));

        SearchRequest request = new SearchRequest(indexName);
        request.source(builder);

        return request;
    }

    private static BoolQueryBuilder getBoolQueryBuilder(final String purification, final String date) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.termQuery("fcltyMngNm.keyword", purification));
        boolQueryBuilder.must(QueryBuilders.rangeQuery("datetime").from(date));
        return boolQueryBuilder;
    }

    public static SearchRequest buildSearchRequest(final String indexName,
                                                   final SearchRequestDTO dto) {
        try {
            final int page = dto.getPage();
            final int size = dto.getSize();
            final int from = page <= 0 ? 0 : page * size;

            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .from(from)
                    .size(size)
                    .postFilter(getQueryBuilder(dto));

            if (dto.getSortBy() != null) {
                builder = builder.sort(
                        dto.getSortBy(),
                        dto.getOrder() != null ? dto.getOrder() : SortOrder.ASC
                );
            }

            final SearchRequest request = new SearchRequest(indexName);
            request.source(builder);

            return request;
        } catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static QueryBuilder getQueryBuilder(final SearchRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        final List<String> fields = dto.getFields();
        if (CollectionUtils.isEmpty(new List[]{fields})) {
            return null;
        }

        if (fields.size() > 1) {
            final MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(dto.getSearchTerm())
                    .type(MultiMatchQueryBuilder.Type.CROSS_FIELDS)
                    .operator(Operator.AND);

            fields.forEach(queryBuilder::field);

            return queryBuilder;
        }

        return fields.stream()
                .findFirst()
                .map(field ->
                        QueryBuilders.matchQuery(field, dto.getSearchTerm())
                                .operator(Operator.AND))
                .orElse(null);
    }

    private static QueryBuilder getQueryBuilder(final String field, final Date date) {
        return QueryBuilders.rangeQuery(field).gte(date);
    }
}
