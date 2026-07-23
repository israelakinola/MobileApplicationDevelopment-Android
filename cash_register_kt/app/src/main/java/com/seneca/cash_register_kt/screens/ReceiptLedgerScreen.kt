package com.seneca.cash_register_kt.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.seneca.cash_register_kt.components.RegisterTopBar
import com.seneca.cash_register_kt.data.InventoryLedger
import com.seneca.cash_register_kt.viewmodel.RegisterConsoleViewModel

/**
 * Lists every completed sale recorded in [RegisterConsoleViewModel.receipts], most-recent-last
 * (the order they were appended in). Tapping a row navigates to `ReceiptDetailScreen` for that
 * entry.
 *
 * When no sales have happened yet, shows a single centered "No Purchase History" card instead
 * of an empty list, so the screen never looks broken/blank on first launch.
 *
 * @param navController used to push the `receiptDetail/{index}` route when a row is tapped, and
 *   to pop back to `ManagerConsoleScreen` via the [RegisterTopBar] back arrow.
 * @param registerViewModel supplies the receipt log this screen renders.
 */
@Composable
fun ReceiptLedgerScreen(navController: NavHostController, registerViewModel: RegisterConsoleViewModel) {
    Scaffold(
        topBar = { RegisterTopBar(title = "Purchase History", onBackTapped = navController::navigateUp) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (registerViewModel.receipts.isEmpty()) {
                EmptyLedgerNotice()
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(registerViewModel.receipts) { receipt ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    val index = registerViewModel.receipts.indexOf(receipt)
                                    navController.navigate("receiptDetail/$index")
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
                                Text(fontSize = 20.sp, text = receipt.itemName)
                                Text("Qty : ${receipt.unitsSold}")
                            }
                            Text(fontSize = 20.sp, text = "$${receipt.amountCharged}")
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

/** Centered placeholder card shown in place of the list when no sales exist yet. */
@Composable
private fun EmptyLedgerNotice() {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = "No Purchase History",
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun ReceiptLedgerScreenPreview() {
    val ledger = InventoryLedger()
    ReceiptLedgerScreen(
        navController = rememberNavController(),
        registerViewModel = RegisterConsoleViewModel(ledger)
    )
}
