package com.seneca.cash_register_kt.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.seneca.cash_register_kt.navigation.BottomNavDestinations
import com.seneca.cash_register_kt.navigation.RegisterDestination

/**
 * Destinations shown in the bar, in left-to-right display order.
 *
 * Only two tabs on purpose: [BottomNavDestinations.RestockConsole] and
 * [BottomNavDestinations.ReceiptLedger] are deliberately *not* tabs here -- they're reached
 * through [BottomNavDestinations.ManagerConsole] instead, mirroring the original app's
 * `ManagerActivity` gate. See the [BottomNavDestinations] doc comment for the full rationale.
 */
private val bottomBarDestinations = listOf(
    BottomNavDestinations.SalesConsole,
    BottomNavDestinations.ManagerConsole
)

/**
 * Picks a compact text glyph to stand in for an icon on a given destination.
 *
 * The project deliberately avoids pulling in the `material-icons-extended` artifact just for
 * two icons, so a single-character/emoji glyph is used instead -- it keeps the dependency
 * footprint small while still giving each tab a distinct visual anchor.
 */
private fun glyphFor(destination: RegisterDestination): String = when (destination) {
    BottomNavDestinations.SalesConsole -> "🛒" // shopping cart
    BottomNavDestinations.ManagerConsole -> "🛠️" // wrench: gateway to Restock/History
    else -> "•" // bullet fallback for any future destination
}

/**
 * Bottom navigation bar shared by every screen in Nimbus Register.
 *
 * Tracks the selected tab locally (it doesn't need to survive process death -- the current
 * [androidx.navigation.NavController] back stack is the real source of truth) and forwards
 * taps to the caller via [onDestinationSelected].
 *
 * @param onDestinationSelected invoked with a [RegisterDestination.route] whenever the user
 *   taps a tab; the caller is expected to call `navController.navigate(route)` in response.
 */
@Composable
fun RegisterBottomBar(onDestinationSelected: (String) -> Unit) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        bottomBarDestinations.forEachIndexed { index, destination ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    onDestinationSelected(destination.route)
                },
                icon = { Text(glyphFor(destination)) },
                label = { Text(destination.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}
