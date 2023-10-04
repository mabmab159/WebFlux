package com.miguel.webflux.pagination;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PageSupport<T> {
    public static final String FIRST_PAGE_NUM = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;

    @JsonProperty
    public long totalPages() {
        return pageSize > 0 ? (totalElements - 1) / pageSize + 1 : 0;
    }

    @JsonProperty
    public boolean first() {
        return pageNumber == Integer.parseInt(FIRST_PAGE_NUM);
    }

    @JsonProperty
    public boolean last() {
        return (long) (pageNumber + 1) * pageSize >= totalElements;
    }

}
