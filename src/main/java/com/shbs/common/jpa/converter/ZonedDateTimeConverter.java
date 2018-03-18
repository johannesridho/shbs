package com.shbs.common.jpa.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.time.ZonedDateTime;

import static com.shbs.common.Constant.ZONE_ID_UTC;

@Converter
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Long> {

    @Override
    public Long convertToDatabaseColumn(ZonedDateTime attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.toInstant().toEpochMilli();
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(Long dbData) {
        if (dbData == null) {
            return null;
        }
        Instant now = Instant.ofEpochMilli(dbData);
        return ZonedDateTime.ofInstant(now, ZONE_ID_UTC);
    }
}
