package com.isac.ecommerce.models.payload.response;

import lombok.Getter;
import lombok.Setter;

public class JwtResponse {

    @Getter
    @Setter
    private String firstname;

    @Getter
    @Setter
    private String lastname;

    @Getter
    @Setter
    private String token;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String role;

    public JwtResponse(String firstname, String lastname, String token, Long id, String email, String role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.token = token;
        this.type = "Bearer";
        this.id = id;
        this.email = email;
        this.role = role;
    }
}
