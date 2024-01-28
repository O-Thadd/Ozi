package com.othadd.ozi.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.othadd.ozi.R

val regularFont = FontFamily(Font(R.font.eras_medium))
val lightFont = FontFamily(Font(R.font.eras_good_normal))
val boldFont = FontFamily(Font(R.font.eras_bold))

// Set of Material typography styles to start with
val Typography = Typography(

    headlineLarge = TextStyle(
        fontFamily = boldFont,
        fontSize = 26.sp
    ),

    titleMedium = TextStyle(
        fontFamily = boldFont,
        fontSize = 22.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = boldFont,
        fontSize = 18.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = regularFont,
        fontSize = 16.sp
    ),

    labelLarge = TextStyle(
        fontFamily = lightFont,
        fontSize = 14.sp
    ),

    labelMedium = TextStyle(
        fontFamily = lightFont,
        fontSize = 12.sp
    ),

    labelSmall = TextStyle(
        fontFamily = lightFont,
        fontSize = 10.sp
    )

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)