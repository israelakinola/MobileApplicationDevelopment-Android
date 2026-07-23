package com.seneca.cash_register_kt.model

/**
 * A single stock-keeping unit sold at the register.
 *
 * Instances are treated as immutable snapshots: rather than mutating [unitsInStock] in place,
 * callers should use [InventoryItem.copy] to produce a new snapshot with an updated count (see
 * `RegisterConsoleViewModel` for why this matters with Compose's observable lists).
 *
 * @property itemName the display name shown in every product list and receipt.
 * @property unitsInStock how many units are currently available to sell.
 * @property unitPrice the price charged per single unit, in dollars.
 */
data class InventoryItem(
    val itemName: String,
    val unitsInStock: Int,
    val unitPrice: Double
)
