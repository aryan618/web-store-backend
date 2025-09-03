package com.aryan.store.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;


    @Column(name = "date", insertable = false, updatable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.MERGE, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<CartItem> items = new LinkedHashSet<>();

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal(0);
        for (CartItem item : items) {
            totalPrice = totalPrice.add(item.getTotalPrice());
        }

        return totalPrice;
    }

    public CartItem getItem(Long productId){
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                return item;
            }
        }

        return null;
    }

    public CartItem addItem(Product product){
        var item= getItem(product.getId());
        if(item!=null){
            item.setQuantity(item.getQuantity()+1);
        }
        else{
            item= new  CartItem();
            item.setProduct(product);
            item.setQuantity(1);
            item.setCart(this);
            items.add(item);
        }

        return item;
    }

    public void removeItem(Long productId){
        var cartItem= getItem(productId);
        items.remove(cartItem);

        cartItem.setCart(null);


    }

    public void clearCartItems(){

        for(CartItem item : items){
            item.setCart(null);
        }

        items.clear();
    }
}