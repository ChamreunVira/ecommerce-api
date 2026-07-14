package com.kh.vira_dev.ecommerceapi.service.impl;

import com.kh.vira_dev.ecommerceapi.dto.request.AddItemRequest;
import com.kh.vira_dev.ecommerceapi.dto.request.UpdateItemRequest;
import com.kh.vira_dev.ecommerceapi.dto.response.CartItemResponse;
import com.kh.vira_dev.ecommerceapi.dto.response.CartResponse;
import com.kh.vira_dev.ecommerceapi.entity.Cart;
import com.kh.vira_dev.ecommerceapi.entity.CartItem;
import com.kh.vira_dev.ecommerceapi.entity.Product;
import com.kh.vira_dev.ecommerceapi.entity.User;
import com.kh.vira_dev.ecommerceapi.exception.ResourceNotFoundException;
import com.kh.vira_dev.ecommerceapi.repository.CartItemRepository;
import com.kh.vira_dev.ecommerceapi.repository.CartRepository;
import com.kh.vira_dev.ecommerceapi.repository.ProductRepository;
import com.kh.vira_dev.ecommerceapi.security.AuthService;
import com.kh.vira_dev.ecommerceapi.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
 
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;

    @Override
    public CartResponse addItem(AddItemRequest request) {

        validateCartItemQuantity(request.getQuantity());

        User user = authService.authenticated();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product"));

        Cart cart = getOrCreateCart(user);
        cart.setUser(user);

        CartItem existingItem = getExistingItem(cart , product);

        if(existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        }else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setCart(cart);
            cartItem.setUnitPrice(BigDecimal.valueOf(product.getPrice()));

            cart.getCartItems().add(cartItem);
        }

        Cart saved = cartRepository.save(cart);
        return toResponse(saved);
    }

    @Override
    public CartResponse getAll() {
        Cart cart = cartRepository.findByUser(authService.authenticated())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        return toResponse(cart);
    }

    @Override
    public CartResponse updateItem(short itemId, UpdateItemRequest request) {

        validateCartItemQuantity(request.getQuantity());

        User user = authService.authenticated();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User"));

        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item"));

        cartItem.setQuantity(request.getQuantity());

        cartItemRepository.save(cartItem);

        return toResponse(cart);
    }

    @Override
    public void removeItem(short itemId) {

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clear() {
        Cart cart = cartRepository.findByUser(authService.authenticated())
                .orElseThrow(() -> new ResourceNotFoundException("User"));
        cart.getCartItems().clear();
        log.info("Delete cart with userId: {}." , cart.getUser().getId());
        cartRepository.save(cart);
    }

    @Override
    public Cart getEntity() {
        User user = authService.authenticated();
        return user.getCart();
    }

    private CartResponse toResponse(Cart cart) {

        return CartResponse
                .builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .cartItems(cart.getCartItems()
                        .stream()
                        .map(this::toCartItemResponse)
                        .toList())
                .totalItems(cart.getCartItems().size())
                .totalAmount(calculateCartTotal(cart).doubleValue())
                .updatedAt(Instant.now())
                .build();
    }

    private CartItemResponse toCartItemResponse(CartItem cartItem) {

        Product product = cartItem.getProduct();

        return CartItemResponse
                .builder()
                .id(cartItem.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productImage(product.getImage().getFirst())
                .unitPrice(BigDecimal.valueOf(product.getPrice()))
                .discountRate(product.getDiscountRate())
                .finalPrice(calculateFinalPrice(cartItem.getProduct()))
                .quantity(cartItem.getQuantity())
                .subtotal(calculateTotalPrice(cartItem))
                .build();
    }

    private void validateCartItemQuantity(int quantity) {
        if(quantity <= 0) {
            throw new IllegalArgumentException("Quantity should be greater than 0");
        }
    }

    private BigDecimal calculateFinalPrice(Product product) {
        BigDecimal price = BigDecimal.valueOf(product.getPrice());

        BigDecimal discount = price.multiply(
                BigDecimal.valueOf(product.getDiscountRate()).divide(
                        new BigDecimal("100") , 10, RoundingMode.HALF_UP
                )
        );

        return price.subtract(discount)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTotalPrice(CartItem cartItem) {
        return calculateFinalPrice(cartItem.getProduct())
                .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
    }

    private BigDecimal calculateCartTotal(Cart cart) {
        return cart.getCartItems()
                .stream()
                .map(this::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2 , RoundingMode.HALF_UP);
    }

    private Cart getOrCreateCart(User user) {
       return user.getCart() != null ? user.getCart() : new Cart();
    }

    private CartItem getExistingItem(Cart cart, Product product) {
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst()
                .orElse(null);
    }
}
