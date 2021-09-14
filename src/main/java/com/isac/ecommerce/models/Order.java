package com.isac.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Order")
@Table(name = "\"Order\"", schema = "public")
@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "user", "carts"})
public class Order extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;

    @Column(name = "quantity", nullable = false)
    @Getter
    @Setter
    private Integer quantity;

    @Column(name = "discount", nullable = false, columnDefinition = "NUMERIC(12,2)")
    @Getter
    @Setter
    private Double discount;

    @Column(name = "imposition", nullable = false, columnDefinition = "NUMERIC(15,2)")
    @Getter
    @Setter
    private Double imposition;

    @Column(name = "subtotal", nullable = false, columnDefinition = "NUMERIC(15,2)")
    @Getter
    @Setter
    private Double subtotal;

    @Column(name = "total", nullable = false, columnDefinition = "NUMERIC(15,2)")
    @Getter
    @Setter
    private Double total;

    @Column(name = "enabled", nullable = false)
    @Getter
    @Setter
    private Boolean enabled;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Getter
    @Setter
    private Set<Cart> carts = new HashSet<>();

}