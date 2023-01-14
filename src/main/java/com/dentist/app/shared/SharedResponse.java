package com.dentist.app.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SharedResponse<T> {
    private Integer statusCode;
    private String statusDescription;
    private T result;
}
