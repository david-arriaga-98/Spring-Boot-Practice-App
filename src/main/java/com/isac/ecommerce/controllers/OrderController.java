package com.isac.ecommerce.controllers;

import com.isac.ecommerce.models.payload.response.SuccessResponse;
import com.isac.ecommerce.models.payload.response.dashboard.IFindProductWithQuantity;
import com.isac.ecommerce.services.OrderService;
import com.isac.ecommerce.utils.exceptions.FluxCommerceException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/test")
    public List<IFindProductWithQuantity> test() {
        return orderService.test();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<SuccessResponse<?>> getAll(Authentication authentication) {
        return ResponseEntity.ok(new SuccessResponse<>(orderService.getAllOrders(authentication)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<?>> getById(Authentication authentication, @PathVariable String id) throws FluxCommerceException {
        return ResponseEntity.ok(new SuccessResponse<>(orderService.getOrderById(authentication, id)));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<SuccessResponse<?>> generateOrder(Authentication authentication) throws FluxCommerceException {
        return ResponseEntity.ok(new SuccessResponse<>(orderService.generateOrder(authentication)));
    }

}
