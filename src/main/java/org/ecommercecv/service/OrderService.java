package org.ecommercecv.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommercecv.common.OrderStatus;
import org.ecommercecv.dto.CartDTO;
import org.ecommercecv.dto.OrderDTO;
import org.ecommercecv.exception.ResourceNotFoundException;
import org.ecommercecv.mapper.CartMapper;
import org.ecommercecv.mapper.OrderMapper;
import org.ecommercecv.model.*;
import org.ecommercecv.repository.OrderRepository;
import org.ecommercecv.repository.ProductRepository;
import org.ecommercecv.repository.UserRepository;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ORDER_SERVICE")
public class OrderService {

    private final OrderRepository orderRepository;

    private final CartService cartService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OrderMapper orderMapper;
    private final CartMapper cartMapper;

    // Add methods for order processing, payment handling, etc.
    // For example:
    // - createOrder
    // - cancelOrder
    // - getOrderDetails
    // - processPayment
    // - etc.

    // Example method
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO createOrder(Long userId, String address, String phoneNumber) {
        // Logic to create an order from the cart
        log.info("Creating order for user: {} with cart: {}", userId);
        // Implementation here...
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: " + userId));
        if (!user.isEmailConfirmation()){
            throw new IllegalStateException("Email not confirmed, please confirm your email");
        }
        CartDTO cartDTO = cartService.getCart(userId);
        Cart cart = cartMapper.toEntity(cartDTO);

        if(cart.getItems().isEmpty()){
            throw new IllegalStateException("Cart is empty, please add items to the cart");
        }

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setStatus(OrderStatus.PREPARE);
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = createOrderItem(cart, order);
        order.setItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userId);
        try{
            emailService.sendOrderConfirmation(savedOrder);
        }catch (MailException e){
            log.error("Failed to send email confirmation for order: {}", savedOrder.getId(), e);
            throw new IllegalStateException("Failed to send email confirmation");
        }
        return orderMapper.toDto(savedOrder);
    }

    private List<OrderItem> createOrderItem(Cart cart, Order order) {
        return cart.getItems().stream().map(
                cartItem -> {
                    Product product = productRepository.findById(cartItem.getProduct().getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + cartItem.getProduct().getId()));
                    if (product.getQuantity() == null) {
                        throw new IllegalStateException("Product quantity is null");
                    }
                    log.info("Product quantity: {}", product.getQuantity());
                    log.info("Cart item quantity: {}", cartItem.getQuantity());
                    if (product.getQuantity() <= cartItem.getQuantity()) {
                        throw new IllegalStateException("Product quantity is not enough");

                    }
                    product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(null, order, product, cartItem.getQuantity(), product.getPrice());
                }
        ).collect(Collectors.toList());
    }


    public List<OrderDTO> getAllOrders(){
        return orderMapper.toDTOs(orderRepository.findAll());
    }


    public List<OrderDTO> getUserOrders(Long userId){
        return orderMapper.toDTOs(orderRepository.findByUserId(userId));
    }

    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDto(updatedOrder);
    }
}
