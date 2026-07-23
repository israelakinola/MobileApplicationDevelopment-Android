package com.seneca.cash_register_kt.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.seneca.cash_register_kt.components.RegisterTopBar
import com.seneca.cash_register_kt.model.ReceiptEntry
import com.seneca.cash_register_kt.viewmodel.RegisterConsoleViewModel

/**
 * Full-detail view of a single [ReceiptEntry], reached by tapping a row in
 * [ReceiptLedgerScreen].
 *
 * @param navController used by the [RegisterTopBar] back arrow to pop back to
 *   [ReceiptLedgerScreen].
 * @param receiptIndex position of the receipt within [RegisterConsoleViewModel.receipts].
 *   Passed as a plain Int (parsed from the nav-route string in `RegisterNavHost`) rather than
 *   the object itself, since NavHost arguments must be primitive/string-serializable.
 * @param registerViewModel supplies the receipt log to read [receiptIndex] out of.
 */
@Composable
fun ReceiptDetailScreen(
    navController: NavHostController,
    receiptIndex: Int,
    registerViewModel: RegisterConsoleViewModel
) {
    val receipt: ReceiptEntry = registerViewModel.receipts[receiptIndex]

    Scaffold(
        topBar = { RegisterTopBar(title = "Receipt Detail", onBackTapped = navController::navigateUp) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    val onTertiaryContainer = MaterialTheme.colorScheme.onTertiaryContainer
                    Text("Item                   : ${receipt.itemName}", color = onTertiaryContainer)
                    Text("Qty purchased   : ${receipt.unitsSold}", color = onTertiaryContainer)
                    Text("Total                  : $${receipt.amountCharged}", color = onTertiaryContainer)
                    Text("Date                   : ${receipt.soldAt}", color = onTertiaryContainer)
                }
            }
        }
    }
}
