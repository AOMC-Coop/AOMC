package com.aomc.coop.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseType<T> {

    private int status;
    private String message;
    private String description;
    private T data;

    public ResponseType(final int status, final String message, String description) {
        this.status = status;
        this.message = message;
        this.description = description;
        this.data = null;
    }

    public static<T> ResponseType<T> res(final int status, final String message, String description) {
        return res(status, message, description, null);
    }

    public static<T> ResponseType<T> res(final int status, final String message, String description, final T t) {
        return ResponseType.<T>builder()
                .data(t)
                .status(status)
                .message(message)
                .description(description)
                .build();
    }


}
