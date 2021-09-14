package com.isac.ecommerce.services;

import com.isac.ecommerce.models.Cart;
import com.isac.ecommerce.models.Order;
import com.isac.ecommerce.models.Product;
import com.isac.ecommerce.models.User;
import com.isac.ecommerce.models.payload.request.AddToCart;
import com.isac.ecommerce.repositories.CartRepository;
import com.isac.ecommerce.repositories.OrderRepository;
import com.isac.ecommerce.repositories.ProductRepository;
import com.isac.ecommerce.utils.exceptions.FluxCommerceException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final UtilService utilService;
    private final AuthService authService;
    private final OrderService orderService;

    public CartService(ProductRepository productRepository, CartRepository cartRepository, OrderRepository orderRepository, UtilService utilService, AuthService authService, OrderService orderService) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.utilService = utilService;
        this.authService = authService;
        this.orderService = orderService;
    }

    public Set<Cart> getShoppingCart(Authentication authentication) throws FluxCommerceException {
        User user = authService.getUserByAuthentication(authentication);
        Optional<Order> optionalOrder = orderRepository.findLastOrder(user.getId());
        Order order;
        if (optionalOrder.isEmpty())
            order = orderService.createEmptyOrder(user);
        else
            order = optionalOrder.get();
        if (!order.getEnabled())
            throw new FluxCommerceException("No hay artículos en el carrito de compras", 400, HttpStatus.BAD_REQUEST);
        return order.getCarts();
    }

    public void quitProductToCart(Authentication authentication, String cartItem) throws FluxCommerceException {
        FluxCommerceException exception = new FluxCommerceException("No se ha encontrado este producto en el carrito", 400, HttpStatus.BAD_REQUEST);
        long cartItemId;
        try {
            cartItemId = utilService.verifyLong(cartItem);
        } catch (NumberFormatException ex) {
            throw exception;
        }
        Optional<Cart> optionalCart = cartRepository.findById(cartItemId);
        User user = authService.getUserByAuthentication(authentication);
        if (optionalCart.isEmpty())
            throw exception;
        Cart cart = optionalCart.get();
        Order order = cart.getOrder();
        if (!order.getUser().getId().equals(user.getId()))
            throw exception;
        else {
            if (!order.getEnabled())
                throw exception;
            order.setQuantity(order.getQuantity() - cart.getQuantity());
            order.setSubtotal(order.getSubtotal() - cart.getTotal());
            order.setDiscount(order.getDiscount() - cart.getDiscount());
            orderRepository.save(order);
            cartRepository.delete(cart);
        }

    }

    public void addProductToCart(Authentication authentication, AddToCart cartRequest) throws FluxCommerceException {
        Optional<Product> optionalProduct = productRepository.findById(cartRequest.getProduct());
        if (optionalProduct.isEmpty())
            throw new FluxCommerceException("No se ha encontrado este producto", 400, HttpStatus.BAD_REQUEST);
        User user = authService.getUserByAuthentication(authentication);
        Optional<Order> optionalOrder = orderRepository.findLastOrder(user.getId());
        Order order;
        if (optionalOrder.isEmpty())
            order = orderService.createEmptyOrder(user);
        else {
            order = optionalOrder.get();
            if (!order.getEnabled())
                order = orderService.createEmptyOrder(user);
        }
        Product product = optionalProduct.get();

        double total = cartRequest.getQuantity() * product.getPrice();
        double discount = cartRequest.getQuantity() * product.getDiscount();

        Optional<Cart> optionalCart = cartRepository.findByProductAndOrder(product, order);
        Cart cart;
        if (optionalCart.isEmpty()) {
            cart = new Cart();
            cart.setProduct(product);
            cart.setDiscount(discount);
            cart.setOrder(order);
            cart.setQuantity(cartRequest.getQuantity());
            cart.setTotal(total);
        } else {
            cart = optionalCart.get();
            cart.setTotal(cart.getTotal() + total);
            cart.setDiscount(cart.getDiscount() + discount);
            cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
        }
        cartRepository.save(cart);

        order.setSubtotal(order.getSubtotal() + total);
        order.setDiscount(order.getDiscount() + discount);
        order.setQuantity(order.getQuantity() + cartRequest.getQuantity());

        orderRepository.save(order);
    }

}
