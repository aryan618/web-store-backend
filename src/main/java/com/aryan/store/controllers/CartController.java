package com.aryan.store.controllers;

import com.aryan.store.dtos.AddItemToCartRequest;
import com.aryan.store.dtos.CartDto;
import com.aryan.store.dtos.CartItemDto;
import com.aryan.store.dtos.UpdateCartItemRequest;
import com.aryan.store.entities.Cart;
import com.aryan.store.entities.CartItem;
import com.aryan.store.entities.Product;
import com.aryan.store.mappers.CartMapper;
import com.aryan.store.repositories.CartRepository;
import com.aryan.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    public CartController(CartRepository cartRepository, CartMapper cartMapper, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        Cart cart = new Cart();
        Cart savedCart = cartRepository.saveAndFlush(cart);
        System.out.println("Cart ID after save: " + savedCart.getId());


        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(savedCart.getId()).toUri();

        CartDto cartDto = cartMapper.toDto(savedCart);
        cartDto.setId(savedCart.getId());
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addCartItem(@PathVariable UUID cartId, @RequestBody AddItemToCartRequest addItemToCartRequest){
        Cart cart= cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            System.out.println("cart not found");
            return ResponseEntity.notFound().build();
        }

        Product product= productRepository.findById(addItemToCartRequest.getId()).orElse(null);

        if(product == null){
            System.out.println("product not found");
            return ResponseEntity.badRequest().build();
        }

//        var cartItems= cart.getItems();
//
//        int count=0;
//
//        CartItem item=null;
//        for(CartItem cartItem : cartItems){
//            if(cartItem.getProduct().getId().equals(product.getId())){
//                count++;
//                item=cartItem;
//                break;
//            }
//        }
//        var item= cart.getItem(product.getId());
//        if(item!=null){
//            item.setQuantity(item.getQuantity()+1);
//        }
//        else{
//            item= new  CartItem();
//            item.setProduct(product);
//            item.setQuantity(1);
//            item.setCart(cart);
//            cart.getItems().add(item);
//        }

        var item= cart.addItem(product);

        cartRepository.save(cart);

       var cartItemDto= cartMapper.toDto(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);

    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId){
        var cart= cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem(@PathVariable("cartId") UUID cartId, @PathVariable("productId") Long productId, @Valid @RequestBody UpdateCartItemRequest request){
        var cart= cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "cart not found")
            );
        }

//        var cartItems= cart.getItems();
//
//        int count=0;
//
//        CartItem item=null;
//        for(CartItem cartItem : cartItems){
//            if(cartItem.getProduct().getId().equals(productId)){
//                count++;
//                item=cartItem;
//                break;
//            }
//        }

        var item= cart.getItem(productId);

        if(item == null){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "product not found")
            );
        }

        item.setQuantity(request.getQuantity());

        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toDto(item));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable UUID cartId, @PathVariable Long productId){
        var cart= cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "cart not found")
            );
        }

        var item= cart.getItem(productId);

        if(item == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "product not found")
            );
        }

        cart.removeItem(productId);
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toDto(item));

    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId){
        var cart= cartRepository.getCartWithItems(cartId).orElse(null);

        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "cart not found"));
        }

        cart.clearCartItems();

        cartRepository.save(cart);
        return ResponseEntity.ok().build();
    }
}
