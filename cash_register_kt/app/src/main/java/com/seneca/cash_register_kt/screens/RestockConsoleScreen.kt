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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.seneca.cash_register_kt.components.RegisterNumericKeypad
import com.seneca.cash_register_kt.components.RegisterTopBar
import com.seneca.cash_register_kt.data.InventoryLedger
import com.seneca.cash_register_kt.model.InventoryItem
import com.seneca.cash_register_kt.viewmodel.RegisterConsoleViewModel
import kotlinx.coroutines.launch

/**
 * Screen for receiving new stock: pick a catalog item, key in how many units arrived on the
 * same on-screen numeric pad used by [SalesConsoleScreen], and confirm to add them to
 * [InventoryItem.unitsInStock].
 *
 * Deliberately mirrors [SalesConsoleScreen]'s exact shape -- summary banner, scrollable
 * catalog, then a keypad + single action button row -- so restocking and selling feel like the
 * same interaction pattern with a different verb at the end (Restock vs. Buy) rather than two
 * different UI paradigms.
 *
 * @param navController used by [RegisterTopBar]'s back arrow to return to `ManagerConsoleScreen`.
 * @param registerViewModel supplies the live catalog and performs the actual restock.
 */
@Composable
fun RestockConsoleScreen(navController: NavHostController, registerViewModel: RegisterConsoleViewModel) {
    // The item currently highlighted by the manager, or null if nothing is selected yet.
    var chosenItem by remember { mutableStateOf<InventoryItem?>(null) }

    // Raw digit string being built by the keypad, same convention as SalesConsoleScreen.
    var incomingUnitsText by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val incomingUnits = incomingUnitsText.toIntOrNull()

    /** Wipes the current selection and keypad entry, returning the form to its empty state. */
    fun discardEntry() {
        chosenItem = null
        incomingUnitsText = ""
    }

    /** Validates the current selection/quantity and, if valid, applies the restock. */
    fun confirmRestock() {
        val item = chosenItem

        if (item == null || incomingUnitsText.isEmpty()) {
            coroutineScope.launch { snackbarHostState.showSnackbar("All Fields Required") }
            return
        }

        val restockSucceeded = registerViewModel.replenishStock(item, incomingUnits!!)
        if (restockSucceeded) {
            discardEntry()
            coroutineScope.launch { snackbarHostState.showSnackbar("Stock updated") }
        } else {
            coroutineScope.launch { snackbarHostState.showSnackbar("Unable to update stock") }
        }
    }

    Scaffold(
        topBar = { RegisterTopBar(title = "Restock", onBackTapped = navController::navigateUp) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(all = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            RestockSummaryBanner(chosenItem, incomingUnitsText)

            Spacer(modifier = Modifier.height(20.dp))

            RestockCatalogList(
                items = registerViewModel.catalog,
                onItemTapped = { chosenItem = it },
                modifier = Modifier.fillMaxWidth().weight(1f)
            )

            Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
                RegisterNumericKeypad(
                    onDigitTapped = { digit -> incomingUnitsText += digit },
                    onClearTapped = ::discardEntry,
                    modifier = Modifier.weight(3f)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = ::confirmRestock,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier.fillMaxHeight()
                ) { Text("Restock") }
            }
        }
    }
}

/**
 * Top-of-screen readout showing which item is selected and how many incoming units have been
 * keyed in so far. Rendered on a tinted [Card] (secondary-container color), matching the sales
 * screen's summary banner exactly except it has no running total (restocking has no price).
 */
@Composable
private fun RestockSummaryBanner(chosenItem: InventoryItem?, incomingUnitsText: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Text(
                text = chosenItem?.itemName ?: "No Product Selected",
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.Start) {
                Text("Qty : ", color = MaterialTheme.colorScheme.onSecondaryContainer)
                Text(
                    text = incomingUnitsText.ifEmpty { "No Quantity Entered" },
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

/** Scrollable catalog list; tapping a row selects it as the restock target. */
@Composable
private fun RestockCatalogList(
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
private fun RestockConsoleScreenPreview() {
    val ledger = InventoryLedger()
    RestockConsoleScreen(
        navController = rememberNavController(),
        registerViewModel = RegisterConsoleViewModel(ledger)
    )
}
