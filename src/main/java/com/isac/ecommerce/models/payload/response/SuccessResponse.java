package com.isac.ecommerce.models.payload.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SuccessResponse<T> extends GenericResponse {

    @Getter
    @Setter
    private T data;

    public SuccessResponse(String message) {
        super("success", 200, message);
        this.data = null;
    }

    public SuccessResponse(T data) {
        super("success", 200, "Petición realizada correctamente");
        this.data = data;
    }

    public SuccessResponse(String status, int code, String message, T data) {
        super(status, code, message);
        this.data = data;
    }

}
