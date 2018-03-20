package com.shbs.api.reservation.exception;

import com.shbs.common.exception.ShbsException;

public class ReservationStartDateHasPassedException extends ShbsException {
    public ReservationStartDateHasPassedException() {
        super("Reservation Start Date has passed");
    }

    @Override
    public String getErrorCode() {
        return "reservation_start_date_has_passed";
    }
}
