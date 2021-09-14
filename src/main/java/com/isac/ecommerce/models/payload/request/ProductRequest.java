package com.isac.ecommerce.models.payload.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

public class ProductRequest {

    @NotBlank
    @Size(min = 3, max = 90)
    @Getter
    @Setter
    private String title;

    @NotBlank
    @Size(min = 3, max = 255)
    @Getter
    @Setter
    private String description;

    @NotNull
    @Getter
    @Setter
    private MultipartFile image;

    @NotNull
    @Positive
    @Digits(integer = 12, fraction = 2)
    @Getter
    @Setter
    private Double price;

    @NotNull
    @Digits(integer = 12, fraction = 2)
    @DecimalMin(value = "0")
    @Getter
    @Setter
    private Double discount;

    @NotNull
    @Positive
    @Digits(integer = 8, fraction = 0)
    @Getter
    @Setter
    private Integer quantity;

    @NotNull
    @Positive
    @Digits(integer = 8, fraction = 0)
    @Getter
    @Setter
    private Long category;

}
