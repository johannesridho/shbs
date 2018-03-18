package com.shbs.exception;


import com.shbs.CommonConstant;

public class NotFoundException extends ShbsException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(Class clazz, String entityId) {
        this(clazz.getSimpleName(), entityId);
    }

    public NotFoundException(String entityName, String entityId) {
        super(String.format(CommonConstant.LOCALE, "%s with Id %s not found.", entityName, entityId));
    }

    @Override
    public String getErrorCode() {
        return "not_found";
    }
}
