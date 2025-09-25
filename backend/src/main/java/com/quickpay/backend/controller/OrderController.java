package com.quickpay.backend.controller;

import com.quickpay.backend.dto.CheckoutResponse;
import com.quickpay.backend.dto.CreateOrderRequest;
import com.quickpay.backend.model.Order;
import com.quickpay.backend.model.Product;
import com.quickpay.backend.repository.OrderRepository;
import com.quickpay.backend.repository.ProductRepository;
import com.quickpay.backend.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentService paymentService;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository, PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest request) throws StripeException {
        Order order = new Order();
        // In a real application, we would get the user from the security context
        // order.setUser(user);

        BigDecimal totalAmount = request.getItems().stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId()).orElseThrow();
                    return product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);
        order.setOrderStatus("PENDING");
        orderRepository.save(order);

        Session checkoutSession = paymentService.createCheckoutSession(order);
        return ResponseEntity.ok(new CheckoutResponse(checkoutSession.getUrl()));
    }
}
