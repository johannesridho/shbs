package com.shbs.common.config.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static com.shbs.common.Constant.DATE_TIME_FORMATTER;
import static com.shbs.common.Constant.ZONE_OFFSET;


public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final LocalDate localDate = LocalDate.parse(p.getValueAsString(), DATE_TIME_FORMATTER);
        return ZonedDateTime.of(localDate, LocalTime.MIN, ZONE_OFFSET);
    }
}
