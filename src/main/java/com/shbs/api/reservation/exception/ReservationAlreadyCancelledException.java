package com.shbs.api.reservation.exception;

import com.shbs.common.exception.ShbsException;

public class ReservationAlreadyCancelledException extends ShbsException {
    public ReservationAlreadyCancelledException() {
        super("Reservation has already been cancelled");
    }

    @Override
    public String getErrorCode() {
        return "reservation_already_cancelled";
    }
}
