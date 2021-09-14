package com.isac.ecommerce.utils.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
public class FluxCommerceException extends Exception {

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private HttpStatus status;

    public FluxCommerceException(String message, int code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }


}
