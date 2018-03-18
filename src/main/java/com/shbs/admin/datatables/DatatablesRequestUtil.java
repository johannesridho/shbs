package com.shbs.admin.datatables;

import com.shbs.admin.jpa.specification.filter.FilterColumn;
import com.shbs.admin.jpa.specification.filter.FilterCriteria;
import com.shbs.admin.jpa.specification.filter.SortedColumn;
import org.springframework.data.domain.PageRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.shbs.common.Constant.LOCALE;


public class DatatablesRequestUtil {

    private static final Pattern pattern = Pattern.compile("columns\\[([0-9]*)?\\]");
    private static final String rangeSearchFilterDelimiter = "-yadcf_delim-";

    public static FilterCriteria toFilterCriteria(HttpServletRequest request) {
        final int columnNumber = getColumnNumber(request);
        final List<FilterColumn> filterColumns = new ArrayList<>();
        final List<SortedColumn> sortedFilterColumns = new LinkedList<>();

        for (int i = 0; i < columnNumber; i++) {
            final String columnData = request.getParameter("columns[" + i + "][data]");
            final String columnName = request.getParameter("columns[" + i + "][name]");
            final String searchTerm = request.getParameter("columns[" + i + "][search][value]");
            final String camelCaseColumnName = getCamelCaseColumnName(columnData, columnName, i);
            final FilterColumn.FilterColumnBuilder filterColumnBuilder = FilterColumn.builder();
            filterColumnBuilder.name(camelCaseColumnName);

            if (isNotBlank(searchTerm)) {
                final String[] splittedSearch = searchTerm.split(rangeSearchFilterDelimiter);
                if (searchTerm.equals(rangeSearchFilterDelimiter)) {
                    filterColumnBuilder.search("");
                } else if (searchTerm.startsWith(rangeSearchFilterDelimiter)) {
                    filterColumnBuilder.searchTo(splittedSearch[1]);
                } else if (searchTerm.endsWith(rangeSearchFilterDelimiter)) {
                    filterColumnBuilder.searchFrom(splittedSearch[0]);
                } else if (searchTerm.contains(rangeSearchFilterDelimiter)) {
                    filterColumnBuilder.searchFrom(splittedSearch[0]);
                    filterColumnBuilder.searchTo(splittedSearch[1]);
                } else {
                    filterColumnBuilder.search(searchTerm);
                }
            }

            final FilterColumn filterColumn = filterColumnBuilder.build();

            filterColumns.add(filterColumn);
        }

        for (int i = 0; i < columnNumber; i++) {
            final String paramSortedCol = request.getParameter("order[" + i + "][column]");
            if (isNotBlank(paramSortedCol)) {
                final String sortedColDirection = request.getParameter("order[" + i + "][dir]");
                final FilterColumn column = filterColumns.get(Integer.parseInt(paramSortedCol));

                if (isNotBlank(sortedColDirection)) {
                    final SortedColumn sortedColumn = SortedColumn.builder()
                            .name(column.getName())
                            .sortDirection(SortedColumn.SortDirection.valueOf(sortedColDirection.toUpperCase(LOCALE)))
                            .build();
                    sortedFilterColumns.add(sortedColumn);
                }
            }
        }

        final List<FilterColumn> cleanedFilterColumns = filterColumns.stream()
                .filter(filterColumn -> !filterColumn.getName().isEmpty())
                .collect(Collectors.toList());

        final String paramStart = request.getParameter("start");
        final String paramLength = request.getParameter("length");
        final int start = isNotBlank(paramStart) ? Integer.parseInt(paramStart) : -1;
        final int length = isNotBlank(paramLength) ? Integer.parseInt(paramLength) : -1;
        return new FilterCriteria(createPageRequest(start, length), cleanedFilterColumns, sortedFilterColumns);
    }

    public static FilterCriteria toFilterCriteriaIgnorePagination(HttpServletRequest request, int maxRow) {
        final PageRequest pageRequest = createPageRequest(0, maxRow);
        final FilterCriteria filterCriteria = toFilterCriteria(request);
        return new FilterCriteria(pageRequest, filterCriteria.getFilterColumns(), filterCriteria.getSortedColumns());
    }

    public static int getDraw(HttpServletRequest request) {
        final String paramDraw = request.getParameter("draw");

        return isNotBlank(paramDraw) ? Integer.parseInt(paramDraw) : -1;
    }

    public static PageRequest createPageRequest(int start, int length) {
        final int pageNumber = (int) Math.ceil((double) start / length);
        return new PageRequest(pageNumber, length);
    }

    private static String getCamelCaseColumnName(String columnDataValue, String columnNameValue, int columnNumber) {
        if (columnDataValue.equals(String.valueOf(columnNumber)) && columnNameValue != null) {
            return toCamelCase(columnNameValue);
        }
        return toCamelCase(columnDataValue);
    }

    private static int getColumnNumber(HttpServletRequest request) {
        int columnNumber = -1;

        for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements(); ) {
            final String param = e.nextElement();
            final Matcher matcher = pattern.matcher(param);
            while (matcher.find()) {
                final Integer col = Integer.parseInt(matcher.group(1));
                if (col > columnNumber) {
                    columnNumber = col;
                }
            }
        }

        if (columnNumber >= 0) {
            columnNumber++;
        }

        return columnNumber;
    }

    private static boolean isNotBlank(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        return !str.trim().isEmpty();
    }

    private static String toCamelCase(String name) {
        if (name.contains("_")) {
            String[] parts = name.split("_");
            StringBuilder camelCaseString = new StringBuilder(parts[0].toLowerCase(LOCALE));
            for (int i = 1; i < parts.length; ++i) {
                camelCaseString.append(capitalize(parts[i]));
            }
            return camelCaseString.toString();
        }
        return name;
    }

    private static String capitalize(final String string) {
        if (string == null) {
            return null;
        }

        final StringBuilder strBuilder = new StringBuilder(string);
        if (strBuilder.length() > 0) {
            strBuilder.setCharAt(0, Character.toTitleCase(strBuilder.charAt(0)));
        }

        return strBuilder.toString();
    }
}
