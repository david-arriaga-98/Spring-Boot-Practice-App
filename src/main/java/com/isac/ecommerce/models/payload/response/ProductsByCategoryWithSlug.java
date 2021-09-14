package com.isac.ecommerce.models.payload.response;


import com.isac.ecommerce.models.Category;
import com.isac.ecommerce.models.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

public class ProductsByCategoryWithSlug extends Category {


    @Getter
    @Setter
    private Page<Product> productPage;

    public ProductsByCategoryWithSlug(Category category, Page<Product> productPage) {
        super();
        super.setId(category.getId());
        super.setSlug(category.getSlug());
        super.setDescription(category.getDescription());
        super.setTitle(category.getTitle());
        super.setImage(category.getImage());
        this.productPage = productPage;
    }

}
