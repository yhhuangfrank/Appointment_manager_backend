package com.frank.hosp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchResponse<T> {

    @JsonProperty
    List<T> content;

    @JsonProperty
    Integer currentPage;

    @JsonProperty
    Long totalPages;
}
