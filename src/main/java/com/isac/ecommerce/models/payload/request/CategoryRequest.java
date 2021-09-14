package com.isac.ecommerce.models.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ToString
public class CategoryRequest {

    @NotNull
    @Getter
    @Setter
    private MultipartFile image;

    @NotBlank
    @Size(min = 3, max = 75)
    @Getter
    @Setter
    private String title;

    @NotBlank
    @Size(min = 3, max = 255)
    @Getter
    @Setter
    private String description;

}
