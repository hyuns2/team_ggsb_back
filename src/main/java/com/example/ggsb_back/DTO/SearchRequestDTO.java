package com.example.ggsb_back.DTO;

import lombok.Builder;
import org.elasticsearch.search.sort.SortOrder;

import java.util.List;


public class SearchRequestDTO extends PagedRequestDTO {
    private List<String> fields;
    private String searchTerm;
    private String sortBy;
    private SortOrder order;


    @Builder
    public SearchRequestDTO(int page, int size, List<String> fields, String searchTerm, String sortBy, SortOrder order) {
        super(page, size);
        this.fields = fields;
        this.searchTerm = searchTerm;
        this.sortBy = sortBy;
        this.order = order;
    }


    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public SortOrder getOrder() {
        return order;
    }

    public void setOrder(SortOrder order) {
        this.order = order;
    }
}
