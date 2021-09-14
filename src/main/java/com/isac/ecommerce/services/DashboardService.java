package com.isac.ecommerce.services;

import com.isac.ecommerce.models.payload.response.dashboard.AdminDashboard;
import com.isac.ecommerce.repositories.CategoryRepository;
import com.isac.ecommerce.repositories.OrderRepository;
import com.isac.ecommerce.repositories.ProductRepository;
import com.isac.ecommerce.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;

    public DashboardService(UserRepository userRepository, ProductRepository productRepository, CategoryRepository categoryRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
    }

    public AdminDashboard getAdminDashboard() {
        AdminDashboard adminDashboard = new AdminDashboard();
        adminDashboard.setArticles(productRepository.count());
        adminDashboard.setUsers(userRepository.count());
        adminDashboard.setCategories(categoryRepository.count());
        adminDashboard.setBestProducts(orderRepository.groupProductsByOrder());
        adminDashboard.setBestSellers(userRepository.getBestSellers());
        adminDashboard.setOrderGeneralDescription(orderRepository.getOrderDescription());
        return adminDashboard;
    }

}
