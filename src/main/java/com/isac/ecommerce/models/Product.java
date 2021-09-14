package com.isac.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Product")
@Table(
        name = "\"Product\"",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "slug")
        }
)
@JsonIgnoreProperties(value = {"category", "quantity", "createdAt", "updatedAt", "carts", "hibernateLazyInitializer", "handler"})
public class Product extends AuditModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "title", length = 90, nullable = false)
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

    @Column(name = "price", nullable = false, columnDefinition = "NUMERIC(12,2)")
    @Getter
    @Setter
    private Double price;

    @Column(name = "discount", nullable = false, columnDefinition = "NUMERIC(12,2)")
    @Getter
    @Setter
    private Double discount;

    @Column(name = "quantity", nullable = false)
    @Getter
    @Setter
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @Getter
    @Setter
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Getter
    @Setter
    private Set<Cart> carts = new HashSet<>();


}
