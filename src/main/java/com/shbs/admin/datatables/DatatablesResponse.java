package com.shbs.admin.datatables;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shbs.admin.jpa.specification.filter.FilterResult;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
@RequiredArgsConstructor
public class DatatablesResponse {

    @JsonProperty("data")
    private final List data;

    @JsonProperty("recordsFiltered")
    private final long recordsFiltered;

    @JsonProperty("recordsTotal")
    private final long recordsTotal;

    @JsonProperty("draw")
    private final int draw;

    public DatatablesResponse(FilterResult filterResult, int draw) {
        final Page page = filterResult.getPage();

        this.data = page.getContent();
        this.recordsFiltered = page.getTotalElements();
        this.recordsTotal = filterResult.getRecordsTotal();
        this.draw = draw;
    }
}
