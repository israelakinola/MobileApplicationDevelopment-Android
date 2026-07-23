package com.seneca.cash_register_kt.ui.theme

import androidx.compose.ui.graphics.Color

/*
 * ---------------------------------------------------------------------------
 * NIMBUS PALETTE
 * ---------------------------------------------------------------------------
 * A custom warm-amber / deep-teal / coral palette built specifically for the
 * Nimbus Register point-of-sale screens. It intentionally avoids the stock
 * Material "Purple/Pink" sample palette that ships with new Compose projects
 * so every screen in this app reads as a distinct, hand-designed product.
 *
 * Naming convention (mirrors Material's tonal-palette convention, renamed):
 *   - The "...Flare" / "...Mist" / "...Bloom" suffix identifies the hue.
 *   - A trailing 80 is a *light, low-contrast* tone meant to sit on a DARK
 *     background (used by the dark color scheme).
 *   - A trailing 40 is a *deep, high-contrast* tone meant to sit on a LIGHT
 *     background (used by the light color scheme).
 * This keeps the same tonal logic Material Design relies on for
 * accessibility, while giving every constant an app-specific identity.
 */

// --- Amber family: the register's primary "action" hue (buy / confirm) ---
val EmberFlare80 = Color(0xFFFFB877) // soft amber for dark-theme primary
val EmberFlare40 = Color(0xFFB35A00) // deep amber for light-theme primary

// --- Teal family: secondary hue used for supporting chrome (nav, cards) ---
val LagoonMist80 = Color(0xFF8FE0D8) // pale teal for dark-theme secondary
val LagoonMist40 = Color(0xFF0F6E67) // deep teal for light-theme secondary

// --- Coral family: tertiary hue reserved for emphasis (totals, confirmations) ---
val CoralBloom80 = Color(0xFFFFA8B7) // pale coral for dark-theme tertiary
val CoralBloom40 = Color(0xFFD1435A) // deep coral for light-theme tertiary

// --- Neutral canvas/surface tones so backgrounds aren't stock Material white/black ---
val DuskCanvasLight = Color(0xFFFFF8F0) // warm ivory app background (light theme)
val DuskCanvasDark = Color(0xFF1B1712) // warm charcoal app background (dark theme)
val DuskSurfaceLight = Color(0xFFFFF3E4) // card/sheet surface (light theme)
val DuskSurfaceDark = Color(0xFF241F19) // card/sheet surface (dark theme)
