package com.frank.common;

public class SystemException extends RuntimeException {

    private Integer statusCode;
    private String errorMessage;

    public static SystemException PARAMETER_INVALID(String errorMessage) {
        return new SystemException(400, errorMessage);
    }

    public SystemException(Integer statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

}
