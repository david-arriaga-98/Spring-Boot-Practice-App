package com.isac.ecommerce.controllers;

import com.isac.ecommerce.models.Category;
import com.isac.ecommerce.models.payload.request.CategoryRequest;
import com.isac.ecommerce.models.payload.response.ProductsByCategoryWithSlug;
import com.isac.ecommerce.models.payload.response.SuccessResponse;
import com.isac.ecommerce.services.CategoryService;
import com.isac.ecommerce.utils.exceptions.FluxCommerceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<SuccessResponse<Page<Category>>> getAllCategories(Pageable pageable) {
        return ResponseEntity.ok(new SuccessResponse<>(categoryService.getAllCategories(pageable)));
    }

    // Return the category and the products with pagination
    @GetMapping(value = "/{slug}")
    public ResponseEntity<SuccessResponse<ProductsByCategoryWithSlug>> getBySlug(@PathVariable String slug, Pageable pageable) throws FluxCommerceException {
        return ResponseEntity.ok(new SuccessResponse<>(categoryService.getBySlug(slug, pageable)));
    }

    @PostMapping(consumes = {"multipart/form-data"}, produces = {"application/json"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SuccessResponse<?>> createCategory(@Valid @ModelAttribute CategoryRequest request) throws FluxCommerceException {
        categoryService.createCategory(request);
        return ResponseEntity.ok(new SuccessResponse<>("Categoría creada correctamente"));
    }


}
