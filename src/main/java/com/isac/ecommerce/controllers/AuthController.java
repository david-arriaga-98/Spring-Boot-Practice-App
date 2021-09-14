package com.isac.ecommerce.controllers;

import com.isac.ecommerce.models.payload.request.SignInRequest;
import com.isac.ecommerce.models.payload.request.SignUpRequest;
import com.isac.ecommerce.models.payload.response.JwtResponse;
import com.isac.ecommerce.models.payload.response.SuccessResponse;
import com.isac.ecommerce.services.AuthService;
import com.isac.ecommerce.utils.exceptions.FluxCommerceException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping(value = "/whoami")
    @PreAuthorize("authenticated")
    public ResponseEntity<SuccessResponse<JwtResponse>> whoAmI(@RequestHeader("Authorization") String token, Authentication authentication) {
        return ResponseEntity.ok(new SuccessResponse<>(
                "success",
                200,
                "Usuario recuperado correctamente",
                authService.getAuthorizedUser(authentication, token.substring(7))
        ));
    }

    @PostMapping(value = "/signin")
    public ResponseEntity<SuccessResponse<JwtResponse>> signIn(@Valid @RequestBody SignInRequest signIn) {
        return ResponseEntity.ok(new SuccessResponse<>(
                "success",
                200,
                "Usuario iniciado correctamente",
                authService.signIn(signIn))
        );
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<SuccessResponse<?>> signUp(@Valid @RequestBody SignUpRequest signUp) throws FluxCommerceException {
        authService.signUp(signUp);
        return ResponseEntity.ok(new SuccessResponse<>("Usuario registrado correctamente!"));
    }

    @PostMapping(value = "/verify/{token}")
    public ResponseEntity<SuccessResponse<?>> verify(@PathVariable String token) throws FluxCommerceException {
        authService.verify(token);
        return ResponseEntity.ok(new SuccessResponse<>("Usuario verificado correctamente!"));
    }

}
