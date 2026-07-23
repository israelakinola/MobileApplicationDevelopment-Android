package com.seneca.cash_register_kt.viewmodel

import androidx.lifecycle.ViewModel
import com.seneca.cash_register_kt.data.InventoryLedger
import com.seneca.cash_register_kt.model.InventoryItem
import com.seneca.cash_register_kt.model.ReceiptEntry

/**
 * Business logic layer for Nimbus Register.
 *
 * Every user-facing screen that touches product/receipt data (`SalesConsoleScreen`,
 * `RestockConsoleScreen`, `ReceiptLedgerScreen`, `ReceiptDetailScreen`) talks to the
 * [InventoryLedger] exclusively through this class rather than mutating [ledger] directly, so
 * all stock/receipt rules live in one place. `ManagerConsoleScreen` is the one exception -- it's
 * a pure navigation gateway with no data of its own, so it doesn't hold a reference to this
 * view model at all.
 *
 * @param ledger the data source this view model reads from and writes to.
 */
class RegisterConsoleViewModel(private val ledger: InventoryLedger) : ViewModel() {

    /** Read-only pass-through so screens don't need a direct [InventoryLedger] reference. */
    val catalog get() = ledger.stockCatalog

    /** Read-only pass-through to the completed-sales log. */
    val receipts get() = ledger.receiptLog

    /**
     * Attempts to sell [unitsRequested] units of [item].
     *
     * On success this deducts stock, appends a [ReceiptEntry], and returns `true`. If there
     * isn't enough stock on hand, nothing is mutated and `false` is returned so the calling
     * screen can show a "not enough stock" message.
     *
     * @param item the catalog item being purchased. Must be an instance currently present in
     *   [catalog] (as returned by the `LazyColumn` the user tapped).
     * @param unitsRequested how many units the customer wants to buy. Must be positive.
     * @return `true` if the sale went through, `false` if stock was insufficient.
     */
    fun processSale(item: InventoryItem, unitsRequested: Int): Boolean {
        if (unitsRequested > item.unitsInStock) {
            return false
        }

        val amountCharged = item.unitPrice * unitsRequested

        // InventoryItem is an immutable data class living inside a Compose
        // mutableStateListOf. Compose only detects a change at a given index when the
        // *object reference* stored there changes -- mutating a field on the existing
        // instance would be invisible to the UI. So we build a fresh copy with the
        // decremented count and write that copy back to the same index.
        val updatedItem = item.copy(unitsInStock = item.unitsInStock - unitsRequested)
        val index = ledger.stockCatalog.indexOf(item)
        ledger.stockCatalog[index] = updatedItem

        ledger.receiptLog.add(
            ReceiptEntry(
                itemName = item.itemName,
                unitsSold = unitsRequested,
                amountCharged = amountCharged
            )
        )
        return true
    }

    /**
     * Adds [unitsToAdd] units of [item] back into stock (e.g. after a new shipment arrives).
     *
     * Unlike [processSale], restocking has no failure condition today, but this still returns
     * a [Boolean] so `RestockConsoleScreen` has a stable contract to react to if validation
     * (e.g. rejecting negative counts) is added later.
     *
     * @param item the catalog item to replenish. Must be an instance currently present in
     *   [catalog].
     * @param unitsToAdd how many units were received.
     * @return `true` once stock has been updated.
     */
    fun replenishStock(item: InventoryItem, unitsToAdd: Int): Boolean {
        val updatedItem = item.copy(unitsInStock = item.unitsInStock + unitsToAdd)
        val index = ledger.stockCatalog.indexOf(item)
        ledger.stockCatalog[index] = updatedItem
        return true
    }
}
