package com.isac.ecommerce.services;

import com.isac.ecommerce.models.Category;
import com.isac.ecommerce.models.Product;
import com.isac.ecommerce.models.payload.request.ProductRequest;
import com.isac.ecommerce.repositories.CategoryRepository;
import com.isac.ecommerce.repositories.ProductRepository;
import com.isac.ecommerce.utils.exceptions.FluxCommerceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UtilService utilService;
    private final ImageManipulationService imageManipulationService;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, UtilService utilService, ImageManipulationService imageManipulationService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.utilService = utilService;
        this.imageManipulationService = imageManipulationService;
    }


    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product findProductBySlug(String slug) throws FluxCommerceException {
        Optional<Product> optionalProduct = productRepository.findBySlug(slug);
        if (optionalProduct.isEmpty())
            throw new FluxCommerceException("El producto que intenta buscar, no existe", 404, HttpStatus.NOT_FOUND);
        return optionalProduct.get();
    }

    public void createProduct(ProductRequest request) throws FluxCommerceException {
        Optional<Category> optionalCategory = categoryRepository.findById(request.getCategory());
        if (optionalCategory.isEmpty())
            throw new FluxCommerceException("La categoría con la que intenta registrar este producto, no existe", 400, HttpStatus.BAD_REQUEST);
        boolean isAValidImage = utilService.verifyIfIsAnImage(Objects.requireNonNull(request.getImage().getOriginalFilename()));
        if (!isAValidImage)
            throw new FluxCommerceException("La imagen ingresada, no es soportada", 400, HttpStatus.BAD_REQUEST);
        // We save the image in the cloud "Cloudinary"
        try {
            String imageName = imageManipulationService.saveImage(request.getImage(), "fluxcommerce/products/");
            Product newProduct = new Product();
            newProduct.setCategory(optionalCategory.get());
            newProduct.setDescription(request.getDescription());
            newProduct.setTitle(request.getTitle());
            newProduct.setPrice(request.getPrice());
            newProduct.setDiscount(request.getDiscount());
            newProduct.setQuantity(request.getQuantity());
            newProduct.setSlug(request.getTitle().toLowerCase().replace(' ', '-') + "-" + utilService.getRandomHexString(5));
            newProduct.setImage(imageName);
            productRepository.save(newProduct);
        } catch (Exception ex) {
            throw new FluxCommerceException("Ha ocurrido un error al tratar de guardar la imagen", 400, HttpStatus.BAD_REQUEST);
        }
    }

}
