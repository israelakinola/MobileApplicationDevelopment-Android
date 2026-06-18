/**
 * AppStore.java
 * PURPOSE: Single source of truth for all app data.
 * WHY A SINGLETON: All 5 Activities need to read and write the same product list
 * and purchase history. A singleton guarantees every Activity is always looking
 * at the same data in memory — no stale copies, no complex Intent passing for lists.
 *
 * HOW TO USE:
 *   AppStore store = AppStore.getInstance();
 *   store.getProducts();         // returns the live product list
 *   store.getPurchaseHistory();  // returns the live history list
 *
 * NEVER call new AppStore() directly — always use getInstance().
 */
package com.seneca.cash_register.store;

import com.seneca.cash_register.model.Product;
import com.seneca.cash_register.model.PurchaseHistory;

import java.util.ArrayList;
import java.util.List;

public class AppStore {

    // The one and only instance — null until first call to getInstance()
    private static AppStore instance;

    // Master product list — shared and mutated by MainActivity and RestockActivity
    private List<Product> products;

    // Grows every time the user completes a purchase in MainActivity
    private List<PurchaseHistory> purchaseHistory;

    // Private constructor — prevents anyone from calling new AppStore()
    private AppStore() {
        // Seed the inventory with the three starting products
        products = new ArrayList<>();
        products.add(new Product("Pants", 20.44, 10));
        products.add(new Product("Shoes", 10.44, 100));
        products.add(new Product("Hats",   5.90, 30));

        // History starts empty; new entries are added in MainActivity on every BUY
        purchaseHistory = new ArrayList<>();
    }

    // Standard lazy singleton — creates the instance only on the first call,
    // returns the existing instance on all subsequent calls
    public static AppStore getInstance() {
        if (instance == null) {
            instance = new AppStore();
        }
        return instance;
    }

    // Returns the live product list — callers must NOT replace this reference,
    // only mutate individual Product objects (e.g. setQuantity)
    public List<Product> getProducts() {
        return products;
    }

    // Returns the live history list — MainActivity appends to this list on every purchase
    public List<PurchaseHistory> getPurchaseHistory() {
        return purchaseHistory;
    }
}
