/**
 * PurchaseHistory.java
 * PURPOSE: Represents one completed purchase transaction.
 * Implements Serializable so it can be passed between Activities via
 * Intent.putExtra() / getSerializableExtra() — specifically from
 * HistoryActivity to HistoryDetailActivity using the key "HISTORY_ITEM".
 * FIELDS:
 *   - productName:  name of the product bought (copied from Product at purchase time)
 *   - quantity:     how many units were bought
 *   - totalPrice:   quantity * unit price calculated at the moment BUY was tapped
 *   - purchaseDate: java.util.Date captured at the moment BUY was tapped
 * USED BY: AppStore (stores the list), MainActivity (creates records),
 *          HistoryAdapter (displays rows), HistoryDetailActivity (shows full detail)
 */
package com.seneca.cash_register.model;

import java.io.Serializable;
import java.util.Date;

public class PurchaseHistory implements Serializable {

    // serialVersionUID is required by Serializable to ensure the class version
    // matches when Android deserializes the object from the Intent extra
    private static final long serialVersionUID = 1L;

    private String productName;
    private int quantity;
    private double totalPrice;
    private Date purchaseDate; // set to new Date() inside MainActivity when BUY is tapped

    // Constructor — a purchase record is fully defined at creation and never changes
    public PurchaseHistory(String productName, int quantity, double totalPrice, Date purchaseDate) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
    }

    // --- Getters only — a purchase record is immutable after creation ---

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }
}
