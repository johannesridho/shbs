package com.shbs.admin.jpa.specification.filter;

import lombok.Value;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Value
public class FilterCriteria {
    private final PageRequest pageRequest;
    private final List<FilterColumn> filterColumns;
    private final List<SortedColumn> sortedColumns;
}
