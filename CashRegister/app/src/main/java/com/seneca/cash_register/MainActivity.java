/**
 * MainActivity.java
 * PURPOSE: The main cash register screen.
 * RESPONSIBILITIES:
 *   - Display the product list from AppStore in a ListView via ProductAdapter
 *   - Accept quantity input via an on-screen numpad (digits 0-9 + C to clear)
 *   - Show a live running total (quantity * selected product price) on every key press
 *   - Validate and process purchases when BUY is tapped
 *   - Navigate to ManagerActivity when MANAGER is tapped
 *   - Refresh the product list in onResume() so restock changes appear on return
 */
package com.seneca.cash_register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.seneca.cash_register.adapter.ProductAdapter;
import com.seneca.cash_register.model.Product;
import com.seneca.cash_register.model.PurchaseHistory;
import com.seneca.cash_register.store.AppStore;

import java.util.Date;
import java.util.List;

public class    MainActivity extends AppCompatActivity {

    // The product the user last tapped in the ListView; null if nothing is selected yet
    private Product selectedProduct;

    // Quantity built digit-by-digit as the user taps numpad buttons (e.g. "12")
    // Stored as a String so we can append characters easily without numeric edge cases
    private String quantityString = "";

    // Adapter kept as a field so onResume() can call notifyDataSetChanged()
    private ProductAdapter productAdapter;

    // UI references
    private TextView tvSelectedProduct;
    private TextView tvQuantity;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Bind views ---
        tvSelectedProduct = findViewById(R.id.tvSelectedProduct);
        tvQuantity        = findViewById(R.id.tvQuantity);
        tvTotal           = findViewById(R.id.tvTotal);
        ListView listViewProducts = findViewById(R.id.listViewProducts);

        // --- Set up the product ListView ---
        // Pull the live product list from the singleton so any changes elsewhere are reflected
        List<Product> products = AppStore.getInstance().getProducts();
        productAdapter = new ProductAdapter(this, products);
        listViewProducts.setAdapter(productAdapter);

        // When the user taps a product row, store it as the selected product and update the UI
        listViewProducts.setOnItemClickListener((parent, view, position, id) -> {
            selectedProduct = products.get(position);
            tvSelectedProduct.setText(selectedProduct.getName());

            // If the user already entered a quantity, recalculate the total immediately
            if (!quantityString.isEmpty()) {
                double total = Integer.parseInt(quantityString) * selectedProduct.getPrice();
                tvTotal.setText(String.format("Total: $%.2f", total));
            }
        });

        // --- Wire up the numpad ---
        wireNumpadButton(R.id.btn1, "1");
        wireNumpadButton(R.id.btn2, "2");
        wireNumpadButton(R.id.btn3, "3");
        wireNumpadButton(R.id.btn4, "4");
        wireNumpadButton(R.id.btn5, "5");
        wireNumpadButton(R.id.btn6, "6");
        wireNumpadButton(R.id.btn7, "7");
        wireNumpadButton(R.id.btn8, "8");
        wireNumpadButton(R.id.btn9, "9");
        wireNumpadButton(R.id.btn0, "0");

        // C button: clear the quantity string and reset both display TextViews to their hints
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(v -> {
            quantityString = "";
            tvQuantity.setText("");
            tvQuantity.setHint(getString(R.string.hint_quantity));
            tvTotal.setText("");
            tvTotal.setHint(getString(R.string.hint_total));
        });

        // --- BUY button ---
        Button btnBuy = findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(v -> processPurchase());

        // --- MANAGER button ---
        Button btnManager = findViewById(R.id.btnManager);
        btnManager.setOnClickListener(v ->
                startActivity(new Intent(this, ManagerActivity.class)));
    }

    /**
     * Attaches an OnClickListener to a numpad digit button.
     * Each press appends the digit to quantityString, updates the quantity TextView,
     * and recalculates the running total if a product is already selected.
     */
    private void wireNumpadButton(int buttonId, String digit) {
        Button btn = findViewById(buttonId);
        btn.setOnClickListener(v -> {
            // Prevent leading zeros (e.g. "007") — ignore a "0" press on an empty string
            if (digit.equals("0") && quantityString.isEmpty()) return;

            quantityString += digit;
            tvQuantity.setText("Quantity: " + quantityString);

            // Recalculate total only when a product has been selected
            if (selectedProduct != null) {
                double total = Integer.parseInt(quantityString) * selectedProduct.getPrice();
                tvTotal.setText(String.format("Total: $%.2f", total));
            }
        });
    }

    /**
     * Validates the current selection and quantity, then completes or rejects the purchase.
     * Validation order matches the assignment spec exactly.
     */
    private void processPurchase() {
        int qty = quantityString.isEmpty() ? 0 : Integer.parseInt(quantityString);

        // 1. Both fields empty
        if (selectedProduct == null && quantityString.isEmpty()) {
            Toast.makeText(this, "All fields are required!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. No product selected
        if (selectedProduct == null) {
            Toast.makeText(this, "Please select a product", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. No quantity entered (or zero)
        if (qty == 0) {
            Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Requested quantity exceeds available stock
        if (qty > selectedProduct.getQuantity()) {
            Toast.makeText(this, "No enough quantity in the stock!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 5. All valid — complete the purchase

        // Deduct purchased quantity from the product's stock in AppStore
        selectedProduct.setQuantity(selectedProduct.getQuantity() - qty);

        // Calculate total and record the transaction
        double total = qty * selectedProduct.getPrice();
        PurchaseHistory record = new PurchaseHistory(
                selectedProduct.getName(), qty, total, new Date());
        AppStore.getInstance().getPurchaseHistory().add(record);

        // Confirm the purchase to the user
        Toast.makeText(this,
                "Thank You for your purchase\nYour purchase is " + qty
                        + " " + selectedProduct.getName()
                        + " for $" + String.format("%.2f", total),
                Toast.LENGTH_LONG).show();

        // Refresh the ListView so the updated stock level is visible immediately
        productAdapter.notifyDataSetChanged();

        // Reset state for the next transaction
        resetForm();
    }

    /**
     * Clears the selection and quantity after a completed purchase or to start fresh.
     */
    private void resetForm() {
        selectedProduct = null;
        quantityString  = "";
        tvSelectedProduct.setText("");
        tvSelectedProduct.setHint(getString(R.string.hint_select_product));
        tvQuantity.setText("");
        tvQuantity.setHint(getString(R.string.hint_quantity));
        tvTotal.setText("");
        tvTotal.setHint(getString(R.string.hint_total));
    }

    /**
     * Called every time MainActivity returns to the foreground (e.g. after the user
     * restocks from RestockActivity). Refreshing here ensures the ListView always
     * shows the current stock levels without needing to restart the Activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        productAdapter.notifyDataSetChanged();
    }
}
