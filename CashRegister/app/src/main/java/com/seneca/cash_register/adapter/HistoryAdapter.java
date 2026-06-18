/**
 * HistoryAdapter.java
 * PURPOSE: Binds the purchase history list from AppStore to a RecyclerView.
 * EXTENDS: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>
 * USED BY: HistoryActivity — displays all completed purchases in a scrollable list.
 * ROW LAYOUT (list_item_history.xml):
 *   - tvProductName: name of the product purchased, large text, left-aligned
 *   - tvQuantity:    quantity bought, smaller/muted text, below the name
 *   - tvTotalPrice:  total price of the purchase, right-aligned
 *
 * ITEM CLICK:
 * Uses an OnItemClickListener interface so HistoryActivity can handle row taps
 * without the adapter needing to know anything about navigation or Intents.
 * HistoryActivity implements the interface and passes itself to setOnItemClickListener().
 */
package com.seneca.cash_register.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.seneca.cash_register.R;
import com.seneca.cash_register.model.PurchaseHistory;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    // Interface that HistoryActivity implements to receive row tap events.
    // Keeping click handling outside the adapter makes the adapter reusable
    // and keeps navigation logic in the Activity where it belongs.
    public interface OnItemClickListener {
        void onItemClick(PurchaseHistory item);
    }

    private final List<PurchaseHistory> historyList;
    private OnItemClickListener listener; // set by HistoryActivity via setOnItemClickListener()

    // Constructor — receives the live history list from AppStore
    public HistoryAdapter(List<PurchaseHistory> historyList) {
        this.historyList = historyList;
    }

    // Called by HistoryActivity so the adapter knows who to notify on row taps
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Inflate the row layout and wrap it in a ViewHolder
    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    // Bind data for the item at the given position into the ViewHolder's TextViews
    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        PurchaseHistory item = historyList.get(position);

        holder.tvProductName.setText(item.getProductName());
        holder.tvQuantity.setText("Qty: " + item.getQuantity());
        holder.tvTotalPrice.setText(String.format("$%.2f", item.getTotalPrice()));

        // Forward row tap to HistoryActivity via the listener interface.
        // Guard against null in case setOnItemClickListener() was never called.
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    // ViewHolder caches the TextViews for each row so RecyclerView doesn't call
    // findViewById() on every bind — this is the key performance benefit of RecyclerView
    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName;
        TextView tvQuantity;
        TextView tvTotalPrice;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity    = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice  = itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}
