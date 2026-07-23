package com.seneca.cash_register_kt.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.seneca.cash_register_kt.components.RegisterTopBar
import com.seneca.cash_register_kt.navigation.BottomNavDestinations

/**
 * Manager gateway screen -- a pure router, with no state or business logic of its own.
 *
 * Mirrors the original Java app's `ManagerActivity`: from the customer-facing Sales screen the
 * user taps into Manager, which offers exactly two ways forward -- open the receipt history, or
 * open the restock console. Everything a manager can *do* still lives entirely in
 * `RestockConsoleScreen` and `ReceiptLedgerScreen`; this screen only decides which one to show
 * next, which is why it needs a [NavHostController] and nothing else (no `RegisterConsoleViewModel`
 * dependency at all).
 *
 * @param navController used to push [BottomNavDestinations.RestockConsole] or
 *   [BottomNavDestinations.ReceiptLedger] when a button is tapped, and to pop back to Sales via
 *   the [RegisterTopBar] back arrow.
 */
@Composable
fun ManagerConsoleScreen(navController: NavHostController) {
    Scaffold(
        topBar = { RegisterTopBar(title = "Manager", onBackTapped = navController::navigateUp) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GatewayButton(
                label = "Restock Inventory",
                onClick = { navController.navigate(BottomNavDestinations.RestockConsole.route) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            GatewayButton(
                label = "View Purchase History",
                onClick = { navController.navigate(BottomNavDestinations.ReceiptLedger.route) }
            )
        }
    }
}

/** Full-width, tertiary-colored button used for both gateway actions on this screen. */
@Composable
private fun GatewayButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary
        ),
        modifier = Modifier.fillMaxWidth()
    ) { Text(label) }
}

@Preview(showBackground = true)
@Composable
private fun ManagerConsoleScreenPreview() {
    ManagerConsoleScreen(navController = rememberNavController())
}
