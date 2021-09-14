package com.isac.ecommerce.services;

import com.isac.ecommerce.models.Order;
import com.isac.ecommerce.models.User;
import com.isac.ecommerce.models.payload.response.dashboard.IFindProductWithQuantity;
import com.isac.ecommerce.repositories.OrderRepository;
import com.isac.ecommerce.utils.exceptions.FluxCommerceException;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class OrderService {

    private final UtilService utilService;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final MailService mailService;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(UtilService utilService, OrderRepository orderRepository, AuthService authService, MailService mailService) {
        this.utilService = utilService;
        this.orderRepository = orderRepository;
        this.authService = authService;
        this.mailService = mailService;
    }

    public List<IFindProductWithQuantity> test() {
        return orderRepository.groupProductsByOrder();
    }

    public List<Order> getAllOrders(Authentication authentication) {
        User user = authService.getUserByAuthentication(authentication);
        return orderRepository.findAllByUserOrderByIdDesc(user);
    }

    public Order getOrderById(Authentication authentication, String id) throws FluxCommerceException {
        long orderID;
        try {
            orderID = utilService.verifyLong(id);
        } catch (NumberFormatException ex) {
            throw new FluxCommerceException("La orden ingresada es inválida", 400, HttpStatus.BAD_REQUEST);
        }
        User user = authService.getUserByAuthentication(authentication);
        Optional<Order> optionalOrder = orderRepository.findByIdAndUser(orderID, user);
        if (optionalOrder.isEmpty())
            throw new FluxCommerceException("La orden no existe", 400, HttpStatus.BAD_REQUEST);
        else
            return optionalOrder.get();
    }

    public Order generateOrder(Authentication authentication) throws FluxCommerceException {
        User user = authService.getUserByAuthentication(authentication);
        Optional<Order> orderOptional = orderRepository.findLastOrder(user.getId());
        if (orderOptional.isEmpty())
            throw new FluxCommerceException("No tienes ningún producto ingresado en tu carrito de compras", 400, HttpStatus.BAD_REQUEST);
        Order order = orderOptional.get();
        if (!order.getEnabled())
            throw new FluxCommerceException("No tienes ningún producto ingresado en tu carrito de compras", 400, HttpStatus.BAD_REQUEST);
        else {
            double imposition = order.getSubtotal() * 0.12;
            order.setImposition(imposition);
            order.setTotal((order.getSubtotal() - order.getDiscount()) + imposition);
            order.setEnabled(false);
            // We send  the email
            String emailId = "d-5e8f11478db946888b67608b7b29bb3a";
            Map<String, String> mailParams = new HashMap<>();
            mailParams.put("current_date", new Date().toString());
            mailParams.put("email", user.getEmail());
            mailParams.put("name", user.getFirstname() + " " + user.getLastname());
            mailParams.put("order_id", order.getId().toString());
            mailParams.put("num_art", order.getQuantity().toString());
            mailParams.put("discount", "$ " + order.getDiscount());
            mailParams.put("imposition", "$ " + order.getImposition());
            mailParams.put("subtotal", "$ " + order.getSubtotal());
            mailParams.put("total", "$ " + order.getTotal());
            try {
                Email to = new Email(user.getEmail(), user.getFirstname() + " " + user.getLastname());
                mailService.sendDynamicEmail("Orden de compra #" + order.getId(), to, mailParams, emailId);
                logger.info("E-mail send");
            } catch (IOException ex) {
                logger.error("No email was sent ");
            }
            return orderRepository.save(order);
        }
    }

    public Order createEmptyOrder(User user) {
        Order newOrder = new Order();
        newOrder.setDiscount((double) 0);
        newOrder.setEnabled(true);
        newOrder.setQuantity(0);
        newOrder.setTotal((double) 0);
        newOrder.setUser(user);
        newOrder.setSubtotal((double) 0);
        newOrder.setImposition((double) 0);
        return orderRepository.save(newOrder);
    }

}
