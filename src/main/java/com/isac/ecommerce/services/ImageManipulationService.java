package com.isac.ecommerce.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.EagerTransformation;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ImageManipulationService {

    private final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dqvyxdjda",
            "api_key", "633735717818849",
            "api_secret", "o5puxcMHF1BSO2a05VvxEczV9B0"
    ));

    public void changeImage() {
        cloudinary.url().transformation(new Transformation().height(860).width(860).crop("scale")).imageTag("fluxcommerce/categories/sg985c8gvbccmjgphqmm.jpg");
    }

    public String saveImage(MultipartFile file, String folderLocation) throws IOException {
        Map response = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.asMap("resource_type", "image",
                        "folder", folderLocation,
                        "eager", List.of(
                                new EagerTransformation().width(860).height(860)
                        )
                ));
        return response.get("public_id") + "." + response.get("format");
    }

}
