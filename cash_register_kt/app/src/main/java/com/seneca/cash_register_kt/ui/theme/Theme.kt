package com.seneca.cash_register_kt.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Dark-mode variant of the Nimbus palette.
 *
 * Uses the "...80" tones from [Color.kt][com.seneca.cash_register_kt.ui.theme] because those
 * are the low-saturation, high-lightness swatches that stay readable on the near-black
 * [DuskCanvasDark] background without blowing out the eyes.
 */
private val NimbusDarkPalette = darkColorScheme(
    primary = EmberFlare80,
    secondary = LagoonMist80,
    tertiary = CoralBloom80,
    background = DuskCanvasDark,
    surface = DuskSurfaceDark
)

/**
 * Light-mode variant of the Nimbus palette.
 *
 * Uses the "...40" tones, which are darker/more saturated so they hold enough contrast
 * against the warm-ivory [DuskCanvasLight] background.
 */
private val NimbusLightPalette = lightColorScheme(
    primary = EmberFlare40,
    secondary = LagoonMist40,
    tertiary = CoralBloom40,
    background = DuskCanvasLight,
    surface = DuskSurfaceLight
)

/**
 * Resolves which [ColorScheme] Nimbus Register should render with.
 *
 * This is a custom helper (rather than inlining the `when` in the theme composable) so the
 * selection logic can be unit-tested or swapped independently of the composable that consumes
 * it. Priority order:
 *   1. Wallpaper-derived "dynamic color" (Android 12+), if the caller opts in via [useDynamicHue].
 *   2. The hand-authored Nimbus dark palette, if [isNight] is true.
 *   3. The hand-authored Nimbus light palette otherwise.
 *
 * @param isNight whether the UI should render in dark mode.
 * @param useDynamicHue whether wallpaper-derived Material You color should be preferred over
 *   the custom Nimbus palette when the OS supports it (Android 12 / API 31+).
 */
@Composable
private fun resolveNimbusColorScheme(isNight: Boolean, useDynamicHue: Boolean): ColorScheme {
    val supportsDynamicHue = useDynamicHue && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    if (supportsDynamicHue) {
        val context = LocalContext.current
        return if (isNight) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    return if (isNight) NimbusDarkPalette else NimbusLightPalette
}

/**
 * Root Material theme for the entire Nimbus Register app.
 *
 * Every screen in this app should be wrapped in [NimbusRegisterTheme] exactly once, at the
 * activity level (see `MainActivity`), so all descendants share one [ColorScheme] and one
 * [NimbusTypography].
 *
 * @param isNight whether to use the dark Nimbus palette. Defaults to following the device's
 *   system-wide dark mode setting.
 * @param useDynamicHue when true, lets Android 12+ wallpaper-based "Material You" color
 *   override the custom Nimbus palette. Defaults to **false** so the app's hand-designed
 *   amber/teal/coral identity is what actually ships, regardless of device/OS version.
 * @param content the screen content to render inside the themed [MaterialTheme].
 */
@Composable
fun NimbusRegisterTheme(
    isNight: Boolean = isSystemInDarkTheme(),
    useDynamicHue: Boolean = false,
    content: @Composable () -> Unit
) {
    val nimbusColorScheme = resolveNimbusColorScheme(isNight = isNight, useDynamicHue = useDynamicHue)

    MaterialTheme(
        colorScheme = nimbusColorScheme,
        typography = NimbusTypography,
        content = content
    )
}
