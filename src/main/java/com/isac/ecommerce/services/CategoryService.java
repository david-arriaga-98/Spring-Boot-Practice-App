package com.isac.ecommerce.services;

import com.isac.ecommerce.models.Category;
import com.isac.ecommerce.models.payload.request.CategoryRequest;
import com.isac.ecommerce.models.payload.response.ProductsByCategoryWithSlug;
import com.isac.ecommerce.repositories.CategoryRepository;
import com.isac.ecommerce.repositories.ProductRepository;
import com.isac.ecommerce.utils.exceptions.FluxCommerceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryService {


    private final CategoryRepository categoryRepository;
    private final UtilService utilService;
    private final ProductRepository productRepository;
    private final ImageManipulationService imageManipulationService;

    public CategoryService(CategoryRepository categoryRepository, UtilService utilService, ProductRepository productRepository, ImageManipulationService imageManipulationService) {
        this.categoryRepository = categoryRepository;
        this.utilService = utilService;
        this.productRepository = productRepository;
        this.imageManipulationService = imageManipulationService;
    }

    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public ProductsByCategoryWithSlug getBySlug(String slug, Pageable pageable) throws FluxCommerceException {
        Optional<Category> category = categoryRepository.findBySlug(slug);
        if (category.isEmpty())
            throw new FluxCommerceException("La categoría que intenta buscar, no existe", 404, HttpStatus.NOT_FOUND);
        return new ProductsByCategoryWithSlug(category.get(), productRepository.findAllByCategory(pageable, category.get()));
    }

    public void createCategory(CategoryRequest request) throws FluxCommerceException {
        boolean isAValidImage = utilService.verifyIfIsAnImage(Objects.requireNonNull(request.getImage().getOriginalFilename()));
        if (!isAValidImage)
            throw new FluxCommerceException("La imagen ingresada, no es soportada", 400, HttpStatus.BAD_REQUEST);
        try {
            String imageName = imageManipulationService.saveImage(request.getImage(), "fluxcommerce/categories/");
            Category category = new Category();
            category.setTitle(request.getTitle());
            category.setDescription(request.getDescription());
            category.setSlug(request.getTitle().toLowerCase().replace(' ', '-') + "-" + utilService.getRandomHexString(5));
            category.setImage(imageName);
            categoryRepository.save(category);
        } catch (IOException ex) {
            throw new FluxCommerceException("Ha ocurrido un error al tratar de guardar la imagen", 400, HttpStatus.BAD_REQUEST);
        }
    }

}
