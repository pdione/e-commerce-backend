package com.embarkx.sbecommerce.controller;

import com.embarkx.sbecommerce.model.Cart;
import com.embarkx.sbecommerce.model.CartItem;
import com.embarkx.sbecommerce.payload.request.CartDTO;
import com.embarkx.sbecommerce.payload.request.ProductDTO;
import com.embarkx.sbecommerce.repository.CartRepository;
import com.embarkx.sbecommerce.service.CartService;
import com.embarkx.sbecommerce.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts() {
        List<CartDTO> cartDTOList = cartService.getAllCarts();
        return new ResponseEntity<>(cartDTOList, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById() {
        String emailId = authUtil.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(emailId);
        Long cartId = cart.getCartId();
        CartDTO cartDTO = cartService.getCart(emailId, cartId);

        cart.getCartItems().forEach(
                c -> c.getProduct().setQuantity(c.getQuantity())
        );
        List<ProductDTO> productDTOS = cart.getCartItems().stream()
                .map(cartItem -> modelMapper.map(cartItem.getProduct(), ProductDTO.class)
                        //productDTO.setQuantity(cartItem.getQuantity());

                ).toList();
        cartDTO.setProducts(productDTOS);
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateCartProductQuantity(@PathVariable Long productId,
                                                             @PathVariable String operation) {
        CartDTO cartDTO = cartService.updateCartProductQuantity(productId,
                operation.equalsIgnoreCase("delete") ? -1 : 1);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/product/{productId}")
    public ResponseEntity<String> deleteFromCart(@PathVariable Long cartId,
                                                 @PathVariable Long productId) {
        String status = cartService.deleteProductFromCart(cartId, productId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
