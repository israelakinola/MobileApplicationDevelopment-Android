package com.seneca.cash_register_kt.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.seneca.cash_register_kt.screens.ManagerConsoleScreen
import com.seneca.cash_register_kt.screens.ReceiptDetailScreen
import com.seneca.cash_register_kt.screens.ReceiptLedgerScreen
import com.seneca.cash_register_kt.screens.RestockConsoleScreen
import com.seneca.cash_register_kt.screens.SalesConsoleScreen
import com.seneca.cash_register_kt.viewmodel.RegisterConsoleViewModel

/**
 * Wires up every screen route for Nimbus Register behind a single [NavHost].
 *
 * All screens share one [RegisterConsoleViewModel] instance (created once in
 * `NimbusRegisterApp` and passed down), so state like the in-progress sale or the receipt log
 * survives navigating between tabs.
 *
 * @param navController controls which route is currently displayed; also passed to individual
 *   screens so they can push further routes (e.g. tapping a receipt row).
 * @param registerViewModel shared view model backing every screen.
 * @param scaffoldPadding padding reserved by the surrounding `Scaffold` (status bar, bottom
 *   navigation bar, etc.) so screen content never renders underneath that chrome.
 */
@Composable
fun RegisterNavHost(
    navController: NavHostController,
    registerViewModel: RegisterConsoleViewModel,
    scaffoldPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavDestinations.SalesConsole.route,
        modifier = Modifier.padding(scaffoldPadding)
    ) {
        composable(BottomNavDestinations.SalesConsole.route) {
            SalesConsoleScreen(navController, registerViewModel)
        }
        composable(BottomNavDestinations.ManagerConsole.route) {
            ManagerConsoleScreen(navController)
        }
        composable(BottomNavDestinations.RestockConsole.route) {
            RestockConsoleScreen(navController, registerViewModel)
        }
        composable(BottomNavDestinations.ReceiptLedger.route) {
            ReceiptLedgerScreen(navController, registerViewModel)
        }
        // Parameterized route: "receiptDetail/3" opens the detail view for receipt index 3.
        composable("receiptDetail/{index}") { backStackEntry ->
            val receiptIndex = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            ReceiptDetailScreen(navController, receiptIndex, registerViewModel)
        }
    }
}
