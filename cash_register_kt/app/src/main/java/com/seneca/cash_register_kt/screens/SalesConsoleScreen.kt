package com.seneca.cash_register_kt.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.seneca.cash_register_kt.components.RegisterNumericKeypad
import com.seneca.cash_register_kt.data.InventoryLedger
import com.seneca.cash_register_kt.model.InventoryItem
import com.seneca.cash_register_kt.viewmodel.RegisterConsoleViewModel
import kotlinx.coroutines.launch

/**
 * The "ring up a sale" screen and the app's default landing destination.
 *
 * Flow: the cashier taps an [InventoryItem] row to select it, keys in a quantity on the
 * on-screen numeric pad, then taps **Buy**. A running total is computed live from the
 * selection and shown at the top. Validation failures (nothing selected, not enough stock)
 * surface as a [androidx.compose.material3.Snackbar] instead of blocking the UI.
 *
 * @param navController used only to keep this screen's signature consistent with the other
 *   nav-hosted screens; sale completion does not navigate anywhere.
 * @param registerViewModel supplies the live catalog and performs the actual sale.
 */
@Composable
fun SalesConsoleScreen(navController: NavController, registerViewModel: RegisterConsoleViewModel) {
    // The item currently highlighted by the cashier, or null if nothing is selected yet.
    var chosenItem by remember { mutableStateOf<InventoryItem?>(null) }

    // Raw digit string being built by the keypad. Kept as String (not Int) so an empty pad
    // can be distinguished from an entered "0", and so leading taps don't need parsing.
    var quantityEntry by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Parses the keypad text to an Int; null while the field is empty or mid-entry-invalid.
    val quantityAsInt = quantityEntry.toIntOrNull()

    // Live running total: price * quantity, or 0.0 if either half of the sale isn't set yet.
    val runningTotal = chosenItem?.let { item ->
        quantityAsInt?.let { qty -> item.unitPrice * qty }
    } ?: 0.0

    /** Wipes the current selection and keypad entry, returning the form to its empty state. */
    fun resetEntry() {
        quantityEntry = ""
        chosenItem = null
    }

    /**
     * Validates the current selection/quantity and, if valid, asks the view model to complete
     * the sale. Shows a snackbar describing whichever outcome occurred (missing fields,
     * insufficient stock, or success).
     */
    fun completeSale() {
        val item = chosenItem
        val qty = quantityAsInt

        if (item == null || quantityEntry.isEmpty()) {
            coroutineScope.launch { snackbarHostState.showSnackbar("All Fields Required") }
            return
        }

        val saleSucceeded = registerViewModel.processSale(item, qty!!)
        if (saleSucceeded) {
            resetEntry()
            coroutineScope.launch { snackbarHostState.showSnackbar("Product Successfully Purchased") }
        } else {
            coroutineScope.launch { snackbarHostState.showSnackbar("Not Enough Stock") }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(all = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            SaleSummaryBanner(chosenItem, quantityEntry, runningTotal)

            Spacer(modifier = Modifier.height(20.dp))

            CatalogList(
                items = registerViewModel.catalog,
                onItemTapped = { chosenItem = it },
                modifier = Modifier.fillMaxWidth().weight(1f)
            )

            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                RegisterNumericKeypad(
                    onDigitTapped = { digit -> quantityEntry += digit },
                    onClearTapped = ::resetEntry,
                    modifier = Modifier.weight(3f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = ::completeSale,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier.fillMaxHeight()
                ) { Text("Buy") }
            }
        }
    }
}

/**
 * Top-of-screen readout showing what's currently selected, the entered quantity, and the
 * live-computed total. Rendered on a tinted [Card] (secondary-container color) so the current
 * in-progress sale visually stands apart from the catalog list below it.
 */
@Composable
private fun SaleSummaryBanner(chosenItem: InventoryItem?, quantityEntry: String, runningTotal: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
                Text(
                    text = chosenItem?.itemName ?: "No Product Selected",
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.Start) {
                    Text("Qty : ", color = MaterialTheme.colorScheme.onSecondaryContainer)
                    Text(
                        text = quantityEntry.ifEmpty { "No Quantity Entered" },
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Text(
                text = "Total : $${"%.2f".format(runningTotal)}",
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

/**
 * Scrollable list of every item in [items]. Tapping a row invokes [onItemTapped] so the caller
 * can update its selection state; this composable holds no selection state of its own.
 */
@Composable
private fun CatalogList(
    items: List<InventoryItem>,
    onItemTapped: (InventoryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemTapped(item) },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.Start) {
                    Text(fontSize = 20.sp, text = item.itemName)
                    Text("Qty : ${item.unitsInStock}")
                }
                Text(fontSize = 20.sp, text = "$${item.unitPrice}")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
private fun SalesConsoleScreenPreview() {
    val ledger = InventoryLedger()
    SalesConsoleScreen(
        navController = rememberNavController(),
        registerViewModel = RegisterConsoleViewModel(ledger)
    )
}
