package com.shbs.common.reservation.exception;

import com.shbs.common.exception.ShbsException;

public class RoomTypeNotAvailableException extends ShbsException {
    public RoomTypeNotAvailableException() {
        super("All the rooms with type you specified have been occupied ");
    }

    @Override
    public String getErrorCode() {
        return "room_type_not_available";
    }
}
