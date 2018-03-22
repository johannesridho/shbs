package com.shbs.common.reservation.exception;

import com.shbs.common.exception.ShbsException;

public class StartDateAfterEndDateException extends ShbsException {
    public StartDateAfterEndDateException() {
        super("Start date must be before end date");
    }

    @Override
    public String getErrorCode() {
        return "start_date_after_end_date";
    }
}
