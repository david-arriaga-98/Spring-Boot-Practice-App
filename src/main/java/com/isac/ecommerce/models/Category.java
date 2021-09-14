package com.isac.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Category")
@Table(
        name = "\"Category\"",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "slug")
        }
)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "products"})
public class Category extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "title", length = 75, nullable = false)
    @Getter
    @Setter
    private String title;

    @Column(name = "description", nullable = false)
    @Getter
    @Setter
    private String description;

    @Column(name = "slug", length = 150, nullable = false)
    @Getter
    @Setter
    private String slug;


    @Column(name = "image", length = 200, nullable = false)
    @Getter
    @Setter
    private String image;


    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "category")
    @Getter
    @Setter
    private Set<Product> products = new HashSet<>();

}
