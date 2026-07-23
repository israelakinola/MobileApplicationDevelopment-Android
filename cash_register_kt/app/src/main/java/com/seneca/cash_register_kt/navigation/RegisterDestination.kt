package com.seneca.cash_register_kt.navigation

/**
 * Describes one navigable screen for use in the bottom navigation bar.
 *
 * @property route the NavHost route string used to navigate to this destination
 *   (see `RegisterNavHost`).
 * @property label the human-readable text shown under the destination's icon.
 */
open class RegisterDestination(val route: String, val label: String)
