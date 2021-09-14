package com.isac.ecommerce.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "User")
@Table(name = "\"User\"",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
@ToString
public class User extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "firstname", nullable = false, length = 65)
    @Getter
    @Setter
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 75)
    @Getter
    @Setter
    private String lastname;

    @Column(name = "email", nullable = false, length = 105)
    @Getter
    @Setter
    private String email;

    @Column(name = "password", nullable = false, length = 135)
    @Getter
    @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 25)
    @Getter
    @Setter
    private RoleEnum role;

    @Column(name = "enabled", nullable = false)
    @Getter
    @Setter
    private Boolean enabled;

    @Column(name = "token", nullable = false, length = 135)
    @Getter
    @Setter
    private String token;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "user")
    @Getter
    @Setter
    private Set<Order> orders = new HashSet<>();


}
