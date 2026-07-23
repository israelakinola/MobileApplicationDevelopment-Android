package com.seneca.cash_register_kt.model

import java.time.LocalDateTime

/**
 * A record of one completed sale, appended to the register's receipt log every time
 * `RegisterConsoleViewModel.processSale` succeeds.
 *
 * @property itemName the [InventoryItem.itemName] that was sold.
 * @property unitsSold how many units were purchased in this transaction.
 * @property amountCharged the total dollar amount charged for this transaction
 *   (`unitPrice * unitsSold` at the time of sale).
 * @property soldAt the moment the sale was recorded; defaults to "now" so call sites don't
 *   need to thread a clock through just to create a receipt.
 */
data class ReceiptEntry(
    val itemName: String,
    val unitsSold: Int,
    val amountCharged: Double,
    val soldAt: LocalDateTime = LocalDateTime.now()
)
