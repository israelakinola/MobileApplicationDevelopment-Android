package com.seneca.cash_register_kt

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.rememberNavController
import com.seneca.cash_register_kt.components.RegisterBottomBar
import com.seneca.cash_register_kt.data.InventoryLedger
import com.seneca.cash_register_kt.navigation.RegisterNavHost
import com.seneca.cash_register_kt.viewmodel.RegisterConsoleViewModel

/**
 * Root composable for the whole app.
 *
 * Responsible for constructing the single, app-lifetime instances that every screen shares:
 *  - the [InventoryLedger] data source,
 *  - the [androidx.navigation.NavHostController] driving navigation, and
 *  - the [RegisterConsoleViewModel] that mediates between the two.
 *
 * It then lays those out inside a [Scaffold] with [RegisterBottomBar] pinned to the bottom and
 * [RegisterNavHost] filling the remaining space.
 */
@Composable
fun NimbusRegisterApp() {
    // remember { } ensures the ledger survives recomposition but is still scoped to this
    // composable's lifetime -- a fresh ledger per app session, not per recomposition.
    val inventoryLedger = remember { InventoryLedger() }
    val navController = rememberNavController()

    val registerViewModel: RegisterConsoleViewModel = viewModel(
        factory = viewModelFactory {
            initializer { RegisterConsoleViewModel(inventoryLedger) }
        }
    )

    Scaffold(
        bottomBar = {
            RegisterBottomBar(
                onDestinationSelected = { route ->
                    // Standard bottom-nav pattern: reuse an existing instance of the tab
                    // instead of stacking a new one, and pop back to the graph's start
                    // destination first so repeated tab-hopping can't grow the back stack
                    // (and, in turn, the number of back-arrow taps needed) without bound.
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        RegisterNavHost(navController, registerViewModel, scaffoldPadding)
    }
}
