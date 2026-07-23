package com.seneca.cash_register_kt.navigation

/**
 * Every navigable screen route in Nimbus Register.
 *
 * Declared as a sealed class purely for namespacing/discoverability (so callers can type
 * `BottomNavDestinations.` and see every option) -- each object independently extends
 * [RegisterDestination], which is what actually supplies its [RegisterDestination.route] and
 * [RegisterDestination.label].
 *
 * Not every destination here is a bottom-bar tab: only [SalesConsole] and [ManagerConsole] are
 * (see `RegisterBottomBar`). [RestockConsole] and [ReceiptLedger] are "gated" behind
 * [ManagerConsole] -- reached only via the buttons on `ManagerConsoleScreen` -- mirroring the
 * original Java app's `MainActivity -> ManagerActivity -> {RestockActivity, HistoryActivity}`
 * hub structure instead of exposing every screen as a flat, equally-reachable tab.
 */
sealed class BottomNavDestinations {
    /** The default screen: ring up a sale against the current catalog. */
    object SalesConsole : RegisterDestination(route = "sales", label = "Sell")

    /** Gateway screen: routes to [RestockConsole] and [ReceiptLedger]. */
    object ManagerConsole : RegisterDestination(route = "manager", label = "Manager")

    /** Add received stock back into the catalog. Reachable only from [ManagerConsole]. */
    object RestockConsole : RegisterDestination(route = "restock", label = "Restock")

    /** Browse past completed sales. Reachable only from [ManagerConsole]. */
    object ReceiptLedger : RegisterDestination(route = "receipts", label = "Receipts")
}
