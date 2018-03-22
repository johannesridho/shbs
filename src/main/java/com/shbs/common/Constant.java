package com.shbs.common;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class Constant {
    public static final Locale LOCALE = Locale.ENGLISH;
    public static final ZoneId ZONE_ID_UTC = ZoneId.of("UTC");
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final int HOUR_DIFFERENCE = 7;
    public static final String TIMEZONE_ABBREVIATION = "WIB";
    public static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(HOUR_DIFFERENCE);

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern(DATE_FORMAT, LOCALE)
            .withZone(ZONE_OFFSET);

    private Constant() {
    }
}
