package com.shbs.common.reservation;

import com.shbs.common.reservation.exception.DateSpecifiedAlreadyPassedException;
import com.shbs.common.reservation.exception.StartDateAfterEndDateException;

import java.time.ZonedDateTime;

public final class ReservationValidationHelper {

    private ReservationValidationHelper() {
    }

    public static void validateReservationTime(ZonedDateTime start, ZonedDateTime end) {
        if (start.isAfter(end)) {
            throw new StartDateAfterEndDateException();
        }

        if (ZonedDateTime.now().isAfter(start)) {
            throw new DateSpecifiedAlreadyPassedException();
        }
    }
}