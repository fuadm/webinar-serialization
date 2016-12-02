package com.hazelcast.training.serialization.avro;


import java.util.ArrayList;

/**
 * Wrapper around {@link ShoppingCart}
 *
 * @author Viktor Gamov on 12/1/16.
 *         Twitter: @gamussa
 * @since 0.0.1
 */
public class ShoppingCartDelegate {
    public ShoppingCart getCart() {
        return cart;
    }

    private ShoppingCart cart;

    private ShoppingCartDelegate(ShoppingCart shoppingCart) {
        this.cart = shoppingCart;
    }

    public static ShoppingCartDelegate of(ShoppingCart cart) {
        cart.setItems(new ArrayList<>());
        return new ShoppingCartDelegate(cart);
    }

    public void addItem(ShoppingCartItem item) {
        cart.getItems().add(item);
        final long l = item.getCost() * item.getQuantity();
        final long prevTotal = cart.getTotal();
        cart.setTotal(prevTotal + l);
    }

    public void removeItem(int index) {
        ShoppingCartItem item = cart.getItems().remove(index);
        final long l = item.getCost() * item.getQuantity();
        final Long prevTotal = cart.getTotal();
        cart.setTotal(prevTotal - l);
    }

    public int size() {
        return cart.getItems().size();
    }
}
