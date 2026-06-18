/**
 * RestockActivity.java
 * PURPOSE: Lets the manager add stock to any product.
 * HOW IT WORKS: Manager selects a product from the ListView, enters a number in
 *               the EditText, and taps OK. The entered value is ADDED to the
 *               product's existing quantity directly in AppStore.
 *               Because AppStore is a singleton, the updated quantity is immediately
 *               visible in MainActivity when it resumes — no Intent passing needed.
 * USED BY: Launched from ManagerActivity via the RESTOCK button.
 */
package com.seneca.cash_register;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.seneca.cash_register.adapter.ProductAdapter;
import com.seneca.cash_register.model.Product;
import com.seneca.cash_register.store.AppStore;

import java.util.List;

public class RestockActivity extends AppCompatActivity {

    // The product the manager tapped in the ListView; null if nothing is selected yet
    private Product selectedProduct;

    private ProductAdapter productAdapter;
    private EditText editTextQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock);

        // Enable the ActionBar back arrow to return to ManagerActivity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.title_restock));
        }

        editTextQty = findViewById(R.id.editTextQty);

        // Set up the product ListView — same adapter used in MainActivity
        List<Product> products = AppStore.getInstance().getProducts();
        productAdapter = new ProductAdapter(this, products);
        ListView listViewProducts = findViewById(R.id.listViewProducts);
        listViewProducts.setAdapter(productAdapter);

        // When the manager taps a product row, store it as the selected product
        listViewProducts.setOnItemClickListener((parent, view, position, id) ->
                selectedProduct = products.get(position));

        // OK button: validate inputs then add stock
        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(v -> processRestock());

        // CANCEL button: discard any changes and return to ManagerActivity
        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> finish());
    }

    /**
     * Validates the current selection and quantity, then adds stock or shows an error.
     * Validation order matches the assignment spec exactly.
     */
    private void processRestock() {
        String qtyInput = editTextQty.getText().toString().trim();

        // 1. Both fields empty
        if (selectedProduct == null && qtyInput.isEmpty()) {
            Toast.makeText(this, "All fields are REQUIRED", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. No product selected
        if (selectedProduct == null) {
            Toast.makeText(this, "Please select a product", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. No quantity entered
        if (qtyInput.isEmpty()) {
            Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. All valid — add the entered amount to the product's current stock.
        //    Because selectedProduct is a reference to the object inside AppStore's list,
        //    calling setQuantity() here mutates the AppStore data directly.
        int added = Integer.parseInt(qtyInput);
        selectedProduct.setQuantity(selectedProduct.getQuantity() + added);

        // Refresh the ListView so the new quantity is visible immediately
        productAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Restock successful", Toast.LENGTH_SHORT).show();

        // Reset for the next restock operation
        selectedProduct = null;
        editTextQty.setText("");
    }

    // Called when the user taps the ActionBar back arrow — returns to ManagerActivity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
