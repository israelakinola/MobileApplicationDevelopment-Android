/**
 * ManagerActivity.java
 * PURPOSE: Manager panel — gateway to the History and Restock features.
 * WHY IT EXISTS: Separates manager-only screens from the customer-facing register,
 *               so the cashier screen stays clean and the manager flow is gated.
 * USED BY: Launched from MainActivity via the MANAGER button.
 *          Launches HistoryActivity and RestockActivity via its own buttons.
 */
package com.seneca.cash_register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        // Enable the back arrow in the ActionBar so the manager can return to MainActivity.
        // The parentActivityName in AndroidManifest.xml tells Android where the up button goes,
        // but getSupportActionBar() must also be told to show the arrow.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.title_manager));
        }

        // HISTORY button: opens the purchase history list
        Button btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class)));

        // RESTOCK button: opens the restock inventory screen
        Button btnRestock = findViewById(R.id.btnRestock);
        btnRestock.setOnClickListener(v ->
                startActivity(new Intent(this, RestockActivity.class)));
    }

    // Called when the user taps the ActionBar back arrow.
    // finish() pops this Activity off the stack and returns to MainActivity.
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
