package com.isac.ecommerce.repositories;

import com.isac.ecommerce.models.Order;
import com.isac.ecommerce.models.User;
import com.isac.ecommerce.models.payload.response.dashboard.IFindProductWithQuantity;
import com.isac.ecommerce.models.payload.response.dashboard.IOrderGeneralDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select * from \"Order\" t where user_id = ?1  order by t.id desc limit 1", nativeQuery = true)
    Optional<Order> findLastOrder(Long id);

    List<Order> findAllByUserOrderByIdDesc(User user);

    Optional<Order> findByIdAndUser(Long id, User user);

    @Query(value = "SELECT sum(o.total) as total, sum(o.quantity) as quantity, p.id as id, p.title as title FROM Order o " +
            "INNER JOIN Cart c ON c.order.id = o.id " +
            "INNER JOIN Product p ON c.product.id = p.id " +
            "GROUP BY p.id ORDER BY total DESC")
    List<IFindProductWithQuantity> groupProductsByOrder();

    @Query(value = "select sum(o.total) as total, sum(o.quantity) as quantity, " +
            "sum(o.imposition) as imposition, count(case when o.enabled = false then 1 end) as disabledOrders, " +
            "count(case when o.enabled then 1 end) as enabledOrders, count(o.enabled) as orders from \"Order\" o", nativeQuery = true)
    IOrderGeneralDescription getOrderDescription();

}