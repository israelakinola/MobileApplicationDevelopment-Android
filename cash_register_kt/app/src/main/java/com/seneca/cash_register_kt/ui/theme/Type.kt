package com.seneca.cash_register_kt.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Type ramp for Nimbus Register.
 *
 * Only the styles the app actually uses are overridden:
 *  - [Typography.bodyLarge] drives ordinary row/label text across every screen.
 *  - [Typography.titleLarge] is bumped up and made semi-bold so item names and the running
 *    total in the sales console stand out against the keypad without needing extra color.
 * Everything else falls back to Material's default type scale.
 */
val NimbusTypography = Typography(
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    )
)
