package com.isac.ecommerce.controllers;

import com.isac.ecommerce.models.Product;
import com.isac.ecommerce.models.payload.request.ProductRequest;
import com.isac.ecommerce.models.payload.response.SuccessResponse;
import com.isac.ecommerce.services.ProductService;
import com.isac.ecommerce.utils.exceptions.FluxCommerceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<SuccessResponse<Page<Product>>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(new SuccessResponse<>(productService.getAllProducts(pageable)));
    }

    @PostMapping(consumes = {"multipart/form-data"}, produces = {"application/json"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<?>> createProduct(@Valid @ModelAttribute ProductRequest request) throws FluxCommerceException {
        productService.createProduct(request);
        return ResponseEntity.ok(new SuccessResponse<>("Producto creado correctamente"));
    }


}
