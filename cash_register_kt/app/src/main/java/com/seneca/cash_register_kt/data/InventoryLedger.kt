package com.seneca.cash_register_kt.data

import androidx.compose.runtime.mutableStateListOf
import com.seneca.cash_register_kt.model.InventoryItem
import com.seneca.cash_register_kt.model.ReceiptEntry

/**
 * In-memory data source for Nimbus Register.
 *
 * This stands in for a real database/network layer: it owns the two lists the rest of the app
 * reads from and writes to, and seeds them with starter stock so the app has something to show
 * on first launch. Because it holds no Android framework references, it can be constructed
 * directly in Compose previews and unit tests without a running app.
 *
 * Both lists are backed by [mutableStateListOf] rather than a plain `MutableList` so that
 * Compose can observe insertions, removals, and index writes and automatically recompose any
 * `LazyColumn` built from them.
 */
class InventoryLedger {

    /** Every product currently known to the register, in display order. */
    val stockCatalog = mutableStateListOf<InventoryItem>()

    /** Chronological log of every completed sale, oldest first. */
    val receiptLog = mutableStateListOf<ReceiptEntry>()

    init {
        // Starter catalog so the Sales/Restock screens aren't empty on first run.
        // Matches the seed data in MAP524/CashRegister's AppStore.java exactly.
        stockCatalog.add(InventoryItem("Pants", unitsInStock = 10, unitPrice = 20.44))
        stockCatalog.add(InventoryItem("Shoes", unitsInStock = 100, unitPrice = 10.44))
        stockCatalog.add(InventoryItem("Hats", unitsInStock = 30, unitPrice = 5.90))
        stockCatalog.add(InventoryItem("Phone", unitsInStock = 40, unitPrice = 6.90))
    }
}
