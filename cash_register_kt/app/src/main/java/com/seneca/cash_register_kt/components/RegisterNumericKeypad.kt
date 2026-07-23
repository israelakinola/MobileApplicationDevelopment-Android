package com.seneca.cash_register_kt.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A 4-row on-screen numeric keypad (1-9, Clear, 0) used to build up a quantity string.
 *
 * Shared by [com.seneca.cash_register_kt.screens.SalesConsoleScreen] (entering how many units to
 * sell) and [com.seneca.cash_register_kt.screens.RestockConsoleScreen] (entering how many units
 * arrived) so both quantity-entry screens look and behave identically.
 *
 * Deliberately dumb: it never parses or validates -- it just reports which digit was tapped
 * (as raw text, so the caller can string-concatenate it) or that Clear was tapped.
 *
 * @param onDigitTapped invoked with a single-character digit string, e.g. `"7"`.
 * @param onClearTapped invoked when the Clear ("C") key is tapped.
 */
@Composable
fun RegisterNumericKeypad(
    onDigitTapped: (String) -> Unit,
    onClearTapped: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keypadButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )

    /** One row of digit buttons, spaced with a fixed 10.dp gutter between each. */
    @Composable
    fun DigitRow(digits: List<String>) {
        Row(modifier = Modifier.fillMaxWidth()) {
            digits.forEachIndexed { index, digit ->
                Button(
                    onClick = { onDigitTapped(digit) },
                    colors = keypadButtonColors,
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) { Text(digit) }
                if (index != digits.lastIndex) Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }

    Column(modifier = modifier) {
        DigitRow(listOf("1", "2", "3"))
        DigitRow(listOf("4", "5", "6"))
        DigitRow(listOf("7", "8", "9"))

        // Final row is irregular (Clear, 0, blank spacer) so it's laid out by hand rather
        // than reusing DigitRow.
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onClearTapped,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                ),
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) { Text("C") }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = { onDigitTapped("0") },
                colors = keypadButtonColors,
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) { Text("0") }

            Spacer(modifier = Modifier.width(10.dp))
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
