package com.frank.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result <T> {

    private String message;

    private T data;

    public static <T> Result<T> ok(T data) {
        return new Result<>("success", data);
    }

    public static <T> Result<T> ok() {
        return Result.ok(null);
    }

    public static <T> Result<T> fail() {
        return new Result<>("failed...", null);
    }
}
