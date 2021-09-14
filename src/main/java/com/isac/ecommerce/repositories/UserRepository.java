package com.isac.ecommerce.repositories;

import com.isac.ecommerce.models.User;
import com.isac.ecommerce.models.payload.response.dashboard.IBestSellers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByToken(String token);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT sum(o.total) as total, sum(o.quantity) as quantity, u.email as email, u.id as id, u.firstname as firstname, u.lastname as lastname FROM Order o " +
            "INNER JOIN User u on u.id = o.user.id " +
            "GROUP BY u.id ORDER BY total DESC")
    List<IBestSellers> getBestSellers();

}