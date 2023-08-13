package com.ksh.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {
    private String resultCode;
    private T result;

    public static <T> Response<T> success() {
        return new Response<T>("SUCCESS", null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }

    public static <T> Response<Void> error(String resultCode) {
        return new Response<Void>(resultCode, null);
    }
}
