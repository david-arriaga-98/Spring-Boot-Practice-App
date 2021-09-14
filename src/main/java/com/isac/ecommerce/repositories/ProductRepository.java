package com.isac.ecommerce.repositories;

import com.isac.ecommerce.models.Category;
import com.isac.ecommerce.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySlug(String slug);

    Optional<Product> findByTitle(String title);

    Boolean existsByTitle(String title);

    Page<Product> findAllByCategory(Pageable pageable, Category category);


}