/**
 * HistoryDetailActivity.java
 * PURPOSE: Shows the full details of a single purchase record.
 * RECEIVES: A PurchaseHistory object via Intent extra with key "HISTORY_ITEM".
 *           This works because PurchaseHistory implements Serializable.
 * DISPLAYS: Product name, total price, and the date/time of the purchase.
 * USED BY: Launched from HistoryActivity when the user taps a history row.
 */
package com.seneca.cash_register;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.seneca.cash_register.model.PurchaseHistory;

public class HistoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        // Enable the ActionBar back arrow to return to HistoryActivity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.title_history_detail));
        }

        // Retrieve the PurchaseHistory object passed from HistoryActivity.
        // getSerializableExtra() works here because PurchaseHistory implements Serializable.
        PurchaseHistory item = (PurchaseHistory) getIntent()
                .getSerializableExtra("HISTORY_ITEM");

        // Bind the purchase fields to the layout TextViews
        TextView tvProduct = findViewById(R.id.tvProduct);
        TextView tvPrice   = findViewById(R.id.tvPrice);
        TextView tvDate    = findViewById(R.id.tvDate);

        // Explicit spaces after labels — Android trims trailing whitespace from string resources
        tvProduct.setText("Product: " + item.getProductName());
        tvPrice.setText("Price: " + String.format("$%.2f", item.getTotalPrice()));
        tvDate.setText("Purchase Date: " + item.getPurchaseDate().toString());
    }

    // Called when the user taps the ActionBar back arrow — returns to HistoryActivity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
