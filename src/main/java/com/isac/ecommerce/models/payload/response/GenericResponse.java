package com.isac.ecommerce.models.payload.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class GenericResponse {

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String message;

    public GenericResponse(String status, int code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
