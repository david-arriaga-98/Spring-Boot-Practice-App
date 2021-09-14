package com.isac.ecommerce.models.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AddToCart {

    @NotNull
    @Positive
    @Digits(integer = 8, fraction = 0)
    @Getter
    @Setter
    private Long product;

    @NotNull
    @Positive
    @Digits(integer = 8, fraction = 0)
    @Getter
    @Setter
    private Integer quantity;

}
