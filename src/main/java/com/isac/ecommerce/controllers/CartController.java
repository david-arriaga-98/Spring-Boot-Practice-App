package com.isac.ecommerce.controllers;

import com.isac.ecommerce.models.payload.request.AddToCart;
import com.isac.ecommerce.models.payload.response.SuccessResponse;
import com.isac.ecommerce.services.CartService;
import com.isac.ecommerce.utils.exceptions.FluxCommerceException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getShoppingCart(Authentication authentication) throws FluxCommerceException {
        return ResponseEntity.ok(new SuccessResponse<>(cartService.getShoppingCart(authentication)));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping(value = "/{cartItem}")
    public ResponseEntity<SuccessResponse<?>> quitProductFromCart(Authentication authentication, @PathVariable String cartItem) throws FluxCommerceException {
        cartService.quitProductToCart(authentication, cartItem);
        return ResponseEntity.ok(new SuccessResponse<>("Artículo quitado correctamente del carrito de compras"));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> addProductToCart(Authentication authentication, @Valid @RequestBody AddToCart cart) throws FluxCommerceException {
        cartService.addProductToCart(authentication, cart);
        return ResponseEntity.ok(new SuccessResponse<>("Artículo agregado correctamente al carrito de compras"));
    }

}
