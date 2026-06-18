/**
 * HistoryActivity.java
 * PURPOSE: Displays all completed purchases in a scrollable RecyclerView list.
 * WHY RecyclerView: Required by the assignment. More efficient than ListView for
 *                   lists that can grow long, and enforces the ViewHolder pattern.
 * USED BY: Launched from ManagerActivity via the HISTORY button.
 *          Launches HistoryDetailActivity when the user taps a history row.
 */
package com.seneca.cash_register;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.seneca.cash_register.adapter.HistoryAdapter;
import com.seneca.cash_register.model.PurchaseHistory;
import com.seneca.cash_register.store.AppStore;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Enable the ActionBar back arrow to return to ManagerActivity
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.title_history));
        }

        // Set up the RecyclerView with a vertical LinearLayoutManager (top to bottom)
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create the adapter with the live purchase history list from AppStore
        HistoryAdapter adapter = new HistoryAdapter(
                AppStore.getInstance().getPurchaseHistory());

        // When a row is tapped, pass the PurchaseHistory object to HistoryDetailActivity.
        // PurchaseHistory implements Serializable, so putExtra() can carry it directly.
        adapter.setOnItemClickListener((PurchaseHistory item) -> {
            Intent intent = new Intent(this, HistoryDetailActivity.class);
            intent.putExtra("HISTORY_ITEM", item);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }

    // Called when the user taps the ActionBar back arrow — returns to ManagerActivity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
