package com.shbs.admin.jpa.specification.filter;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FilterColumn {

    private final String name;
    private final String search;
    private final String searchFrom;
    private final String searchTo;
}
