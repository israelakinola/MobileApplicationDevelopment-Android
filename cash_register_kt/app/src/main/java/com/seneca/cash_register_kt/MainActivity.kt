package com.seneca.cash_register_kt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.seneca.cash_register_kt.ui.theme.NimbusRegisterTheme

/**
 * Single-activity host for Nimbus Register.
 *
 * All UI is Jetpack Compose, so this activity's only job is to enable edge-to-edge rendering
 * and hand off to [NimbusRegisterApp] (wrapped in [NimbusRegisterTheme]) via [setContent].
 * Navigation between screens happens entirely inside Compose -- see `RegisterNavHost` -- so no
 * further activities or fragments are needed.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NimbusRegisterTheme {
                NimbusRegisterApp()
            }
        }
    }
}
