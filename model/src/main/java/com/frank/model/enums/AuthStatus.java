package com.frank.model.enums;

public enum AuthStatus {

    NO_AUTH(0, "unauthenticated"),
    UNDER_AUTH(1, "authenticating..."),
    AUTH_SUCCESS(2, "auth success"),
    AUTH_FAIL(-1, "auth failed..");

    private Integer status;
    private String statusName;

    AuthStatus(Integer status, String statusName) {
        this.status = status;
        this.statusName = statusName;
    }

    public static String getStatusNameByStatusCode(Integer status) {
        for (AuthStatus authStatus : AuthStatus.values()) {
            if (authStatus.status.equals(status)) {
                return authStatus.statusName;
            }
        }
        return "";
    }
}
