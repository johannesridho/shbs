package com.shbs.admin.jpa.specification.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.shbs.common.Constant.LOCALE;


@RequiredArgsConstructor
public abstract class FilterSpecification<T> implements Specification<T> {
    protected final FilterCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        query.orderBy(getOrders(root, query, cb));

        final List<Predicate> predicates = new ArrayList<>();

        for (FilterColumn column : criteria.getFilterColumns()) {
            final Path dbColumnName = getColumnName(root, query, cb, column.getName());

            if (!isEmpty(column.getSearchFrom())) {
                predicates.add(generateSearchFromPredicate(cb, dbColumnName, column));
            }

            if (!isEmpty(column.getSearchTo())) {
                predicates.add(generateSearchToPredicate(cb, dbColumnName, column));
            }

            if (!isEmpty(column.getSearch())) {
                predicates.add(generateLikePredicate(cb, dbColumnName, column));
            }
        }

        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }

    protected Predicate generateSearchFromPredicate(CriteriaBuilder cb, Path dbColumnName, FilterColumn datatablesColumn) {
        final String searchFrom = datatablesColumn.getSearchFrom();

        if (isDateColumn(datatablesColumn.getName())) {
            return cb.greaterThanOrEqualTo(dbColumnName, ZonedDateTime.parse(searchFrom));
        } else if (isValidNumber(searchFrom)) {
            return cb.greaterThanOrEqualTo(dbColumnName, searchFrom);
        }

        return cb.conjunction();
    }

    protected Predicate generateSearchToPredicate(CriteriaBuilder cb, Path dbColumnName, FilterColumn datatablesColumn) {
        final String searchTo = datatablesColumn.getSearchTo();

        if (isDateColumn(datatablesColumn.getName())) {
            return cb.lessThanOrEqualTo(dbColumnName, ZonedDateTime.parse(searchTo));
        } else if (isValidNumber(searchTo)) {
            return cb.lessThanOrEqualTo(dbColumnName, searchTo);
        }

        return cb.conjunction();
    }

    protected Predicate generateLikePredicate(CriteriaBuilder cb, Path dbColumnName, FilterColumn column) {
        final Expression<String> dbColumnNameAsString = dbColumnName.as(String.class);

        return cb.like(dbColumnNameAsString, getSearchString(column));
    }

    protected String getSearchString(FilterColumn column) {
        return isExactEqualColumn(column.getName()) ?
                column.getSearch() : getLikePattern(column.getSearch());
    }

    protected String getLikePattern(String searchTerm) {
        if (searchTerm == null || searchTerm.isEmpty()) {
            return "%";
        } else {
            return searchTerm.toLowerCase(LOCALE) + "%";
        }
    }

    protected abstract <Y> Path<Y> getColumnName(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb, String name);

    protected abstract boolean isDateColumn(String name);

    protected boolean isExactEqualColumn(String name) {
        return false;
    }

    protected List<Order> getOrders(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        final List<Order> orders = new ArrayList<>();

        for (final SortedColumn column : criteria.getSortedColumns()) {
            final Order order = column.getSortDirection().equals(SortedColumn.SortDirection.ASC) ?
                    cb.asc(getColumnName(root, query, cb, column.getName())) :
                    cb.desc(getColumnName(root, query, cb, column.getName()));

            orders.add(order);
        }

        return orders;
    }

    protected boolean isEmpty(String string) {
        return string == null || string.trim().equals("");
    }

    protected boolean isValidNumber(String str) {
        return str.matches("^-?\\d+$");
    }
}
