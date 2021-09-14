package com.isac.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "Cart")
@Table(name = "\"Cart\"", schema = "public")
@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "order"})
public class Cart extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @Getter
    @Setter
    private Order order;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @Getter
    @Setter
    private Product product;

    @Column(name = "total", nullable = false, columnDefinition = "NUMERIC(15,2)")
    @Getter
    @Setter
    private Double total;

    @Column(name = "quantity", nullable = false)
    @Getter
    @Setter
    private Integer quantity;

    @Column(name = "discount", nullable = false, columnDefinition = "NUMERIC(12,2)")
    @Getter
    @Setter
    private Double discount;

}
