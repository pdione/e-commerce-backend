package com.embarkx.sbecommerce.service.impl;

import com.embarkx.sbecommerce.exception.ApiException;
import com.embarkx.sbecommerce.exception.ResourceNotFoundException;
import com.embarkx.sbecommerce.model.Cart;
import com.embarkx.sbecommerce.model.CartItem;
import com.embarkx.sbecommerce.model.Product;
import com.embarkx.sbecommerce.payload.request.CartDTO;
import com.embarkx.sbecommerce.payload.request.ProductDTO;
import com.embarkx.sbecommerce.repository.CartItemRepository;
import com.embarkx.sbecommerce.repository.CartRepository;
import com.embarkx.sbecommerce.repository.ProductRepository;
import com.embarkx.sbecommerce.service.CartService;
import com.embarkx.sbecommerce.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        // Find existing cart or create one
        Cart cart = createCart();
        // Retrieve product details
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
        // Perform validations
        CartItem cartItem = cartItemRepository.findCarItemByProductIdAndCartId(
                cart.getCartId(),
                productId);

        if (cartItem != null) {
            throw new ApiException("Product " + product.getProductName() + " already exists in cart");
        }

        if (product.getQuantity() == 0) {
            throw new ApiException("Product " + product.getProductName() + " is out of stock");
        }

        if (quantity > product.getQuantity()) {
            throw new ApiException("Please make an order fo the " + product.getProductName() + " less than or equal to the stock quantity: " + quantity);
        }
        // Create CartItem
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(quantity);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        // Save CartItem
        cartItemRepository.save(newCartItem);
        //product.setQuantity(product.getQuantity() - quantity);

        // Update total price
        cart.setTotalPrice(cart.getTotalPrice() + (newCartItem.getProductPrice() * newCartItem.getQuantity()));
        // Return updated cart
        Cart savedCart = cartRepository.save(cart);
        CartDTO cartDTO = modelMapper.map(savedCart, CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productDTOStream = cartItems.stream()
                .map(item -> {
                    ProductDTO map = modelMapper.map(item.getProduct(), ProductDTO.class);
                    map.setQuantity(item.getQuantity());
                    return map;
                });
        cartDTO.setProducts(productDTOStream.collect(Collectors.toList()));
        return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            throw new ApiException("No carts found");
        }
        return carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    List<ProductDTO> productDTOS = cart.getCartItems().stream()
                            .map(cartItem -> {
                                ProductDTO productDTO = modelMapper.map(cartItem.getProduct(), ProductDTO.class);
                                productDTO.setQuantity(cartItem.getQuantity());
                                return productDTO;
                            }).toList();
                    cartDTO.setProducts(productDTOS);

                    return cartDTO;
                }).toList();
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
        if (cart == null) {
            throw new ResourceNotFoundException("Cart", "CartId", cartId);
        }
        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    @Transactional
    public CartDTO updateCartProductQuantity(Long productId, Integer quantity) {

        String emailId = authUtil.loggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(emailId);
        Long cartId = userCart.getCartId();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "CartId", cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));

        if (product.getQuantity() == 0) {
            throw new ApiException("Product " + product.getProductName() + " is out of stock");
        }

        if (quantity > product.getQuantity()) {
            throw new ApiException("Please make an order fo the " + product.getProductName() + " less than or equal to the stock quantity: " + quantity);
        }
        CartItem cartItem = cartItemRepository.findCarItemByProductIdAndCartId(
                cart.getCartId(),
                productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "ProductId", productId);
        }

        int newQuantity = cartItem.getQuantity() + quantity;
        if (newQuantity < 0) {
            throw new ApiException("The quantity cannot be less than 0 in the Cart");
        }

        if (newQuantity == 0) {
            deleteProductFromCart(cart.getCartId(), productId);
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(newQuantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * cartItem.getQuantity()));
            cartRepository.save(cart);
        }

        CartItem updatedCartItem = cartItemRepository.save(cartItem);
        if (updatedCartItem.getQuantity() == 0) {
            cartItemRepository.deleteById(updatedCartItem.getCartItemId());
        }

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> productDTOS = cart.getCartItems()
                .stream().map(item -> {
                    ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                    productDTO.setQuantity(item.getQuantity());
                    return productDTO;
                }).toList();
        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }

    @Override
    @Transactional
    public String deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "CartId", cartId));
        CartItem cartItem = cartItemRepository.findCarItemByProductIdAndCartId(cart.getCartId(), productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "ProductId", productId);
        }
        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartItemRepository.deleteCarItemByProductIdAndCartId(cartId, productId);
        cartRepository.save(cart);
        return "Product " + cartItem.getProduct().getProductName() + " removed from cart";
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "CartId", cartId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
        CartItem cartItem = cartItemRepository.findCarItemByProductIdAndCartId(cart.getCartId(), productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "ProductId", productId);
        }

        // 1000 - (100 * 2) = 800
        double oldPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

        // 200
        cartItem.setProductPrice(product.getSpecialPrice());

        // 800 + (200 * 2) = 1200
        cart.setTotalPrice(oldPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.save(cartItem);
        cartRepository.save(cart);

    }

    Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if (userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        return cartRepository.save(cart);
    }


}
