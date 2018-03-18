package com.shbs.admin.jpa.specification.filter;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SortedColumn {
    private final String name;
    private final SortDirection sortDirection;

    public enum SortDirection {
        ASC, DESC;
    }
}
