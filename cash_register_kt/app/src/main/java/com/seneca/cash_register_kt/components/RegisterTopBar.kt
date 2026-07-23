package com.seneca.cash_register_kt.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

/**
 * Back-arrow title bar shown on every screen except [com.seneca.cash_register_kt.screens.SalesConsoleScreen].
 *
 * Mirrors the ActionBar "up" arrow every non-Main `Activity` had in the original Java app
 * (`getSupportActionBar().setDisplayHomeAsUpEnabled(true)`, wired to `onSupportNavigateUp()`).
 * Compose has no ActionBar, so this recreates the same affordance explicitly: a titled bar with
 * a tappable back glyph, rather than relying solely on the system back gesture/button.
 *
 * @param title screen title shown centered in the bar (e.g. "Manager", "Restock").
 * @param onBackTapped invoked when the back glyph is tapped; callers pass
 *   `navController::navigateUp` (or an equivalent lambda) to pop the back stack.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterTopBar(title: String, onBackTapped: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackTapped) {
                // A plain glyph instead of Icons.AutoMirrored.Filled.ArrowBack so the project
                // doesn't need the material-icons-extended dependency for a single icon.
                Text("←", style = MaterialTheme.typography.titleLarge)
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    )
}
