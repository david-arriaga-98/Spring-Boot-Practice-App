package com.isac.ecommerce.controllers;

import com.isac.ecommerce.models.payload.response.GenericResponse;
import com.isac.ecommerce.utils.exceptions.FluxCommerceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FluxCommerceException.class)
    protected ResponseEntity<GenericResponse> handleConflict(FluxCommerceException ex) {
        return new ResponseEntity<>(new GenericResponse("error", ex.getCode(), ex.getMessage()), ex.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<GenericResponse> handleBadCredentials() {
        return new ResponseEntity<>(new GenericResponse("error", 403, "Correo electrónico y/o Contraseña incorrectos"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DisabledException.class)
    protected ResponseEntity<GenericResponse> handleDisabledUsed(DisabledException ex) {
        return new ResponseEntity<>(new GenericResponse("error", 403, "El usuario con el cuál desea ingresar, no ha verificado su correo electrónico"), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<GenericResponse> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>(new GenericResponse("error", 403, "Su acceso a este recurso, ha sido denegado"), HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new GenericResponse("error", status.value(), ex.getMessage()), status);
    }
}
