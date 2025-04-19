package org.ecommercecv.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ecommercecv.dto.CartDTO;
import org.ecommercecv.exception.InsufficientStockException;
import org.ecommercecv.exception.ResourceNotFoundException;
import org.ecommercecv.mapper.CartMapper;
import org.ecommercecv.model.Cart;
import org.ecommercecv.model.CartItem;
import org.ecommercecv.model.Product;
import org.ecommercecv.model.User;
import org.ecommercecv.repository.CartRepository;
import org.ecommercecv.repository.ProductRepository;
import org.ecommercecv.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CART_SERVICE")
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Transactional(rollbackFor = Exception.class)
    public CartDTO addToCart(Long userId, Long productId, Integer quantity){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Not enough product in stock");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElse(new Cart(null, user, new ArrayList<>()));

        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()){
            CartItem cartItem = existingCartItem.get();
            // add quantity to existing cart item
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        else {
            CartItem cartItem = new CartItem(null, cart, product, quantity);
            cart.getItems().add(cartItem);
        }
        Cart savedCart = cartRepository.save(cart);
        return cartMapper.toDto(savedCart);
    }

    public CartDTO getCart(Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return cartMapper.toDto(cart);
    }

    public void clearCart(Long userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        cart.getItems().clear(); // clear the cart items as having ArrayList type
        cartRepository.save(cart);
    }

//    update
    @Transactional
    public void removeCartItem(Long userId, Long productId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()-> new ResourceNotFoundException("Cart not found"));
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));

        cartRepository.save(cart);
    }




}
