/**
 * ProductAdapter.java
 * PURPOSE: Binds the product list from AppStore to a ListView row (list_item_product.xml).
 * EXTENDS: ArrayAdapter<Product> — handles list management; we only override getView()
 *          to populate each row's TextViews with the Product's data.
 * USED BY: MainActivity and RestockActivity — both show the same product ListView.
 * ROW LAYOUT (list_item_product.xml):
 *   - tvName:     product name, large text, left-aligned
 *   - tvPrice:    unit price, smaller/muted text, below the name
 *   - tvQuantity: current stock quantity, right-aligned
 */
package com.seneca.cash_register.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.seneca.cash_register.R;
import com.seneca.cash_register.model.Product;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private final Context context;
    private final List<Product> productList;

    // Constructor — stores context and list reference so getView() can use them
    public ProductAdapter(Context context, List<Product> productList) {
        super(context, R.layout.list_item_product, productList);
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Reuse an existing row view if available; otherwise inflate a new one.
        // This is the standard ListView recycling pattern — avoids creating a new
        // View object on every scroll, which would cause janky performance.
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.list_item_product, parent, false);
        }

        // Get the Product for this row position
        Product product = productList.get(position);

        // Bind each field to its corresponding TextView in the row layout
        TextView tvName     = convertView.findViewById(R.id.tvName);
        TextView tvPrice    = convertView.findViewById(R.id.tvPrice);
        TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);

        tvName.setText(product.getName());
        tvPrice.setText(String.format("$%.2f", product.getPrice()));
        tvQuantity.setText("Qty: " + product.getQuantity());

        return convertView;
    }
}
