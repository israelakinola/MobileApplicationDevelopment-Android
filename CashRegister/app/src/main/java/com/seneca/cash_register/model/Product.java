/**
 * Product.java
 * PURPOSE: Represents one item in the store inventory.
 * FIELDS:
 *   - name:     display name of the product (e.g. "Pants")
 *   - price:    price per unit as a double (e.g. 20.44)
 *   - quantity: current stock level — mutable because purchases deduct stock
 *               and RestockActivity adds to it
 * USED BY: AppStore (holds the master list), MainActivity (display + deduct),
 *          RestockActivity (add stock), ProductAdapter (binds to ListView rows)
 */
package com.seneca.cash_register.model;

public class Product {

    private String name;
    private double price;
    private int quantity; // changes on every purchase and restock

    // Constructor — sets all three fields at creation time
    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // --- Getters ---

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // --- Setters ---

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Called in MainActivity to deduct stock after a purchase,
    // and in RestockActivity to add stock when the manager restocks
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
