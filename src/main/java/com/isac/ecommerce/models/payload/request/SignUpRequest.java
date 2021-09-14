package com.isac.ecommerce.models.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@ToString
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 65)
    @Getter
    @Setter
    private String firstname;

    @NotBlank
    @Size(min = 3, max = 75)
    @Getter
    @Setter
    private String lastname;

    @NotBlank
    @Size(max = 105)
    @Getter
    @Setter
    private String email;

    @NotBlank
    @Size(min = 6, max = 40)
    @Getter
    @Setter
    private String password;

}
