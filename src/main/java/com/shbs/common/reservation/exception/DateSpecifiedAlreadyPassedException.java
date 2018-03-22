package com.shbs.common.reservation.exception;

import com.shbs.common.exception.ShbsException;

public class DateSpecifiedAlreadyPassedException extends ShbsException {
    public DateSpecifiedAlreadyPassedException() {
        super("Start date and end date must be in the future");
    }

    @Override
    public String getErrorCode() {
        return "date_specified_already_passed";
    }
}
