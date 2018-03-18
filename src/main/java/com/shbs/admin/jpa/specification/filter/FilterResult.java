package com.shbs.admin.jpa.specification.filter;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;

@Value
@RequiredArgsConstructor
@Builder
public class FilterResult<T> {
    private final Page<T> page;
    private final long recordsTotal;
}
