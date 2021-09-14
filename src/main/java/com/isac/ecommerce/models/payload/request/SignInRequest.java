package com.isac.ecommerce.models.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
public class SignInRequest {

    @NotBlank
    @Getter
    @Setter
    private String email;

    @NotBlank
    @Getter
    @Setter
    private String password;

}
