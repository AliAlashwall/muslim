package com.example.muslim.presentation.designSystem.typography

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.muslim.R

val CairoFontFamily = FontFamily(
    Font(R.font.cairo_regular, FontWeight.Normal),
    Font(R.font.cairo_medium, FontWeight.Medium),
    Font(R.font.cairo_semi_bold, FontWeight.SemiBold),
    Font(R.font.cairo_bold, FontWeight.Bold)
)

val DefaultTextStyle = MuslimTextStyle(
    headline = SizedTextStyle(
        large = TextStyle(
            fontSize = 34.sp,
            lineHeight = 40.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold
        ),
        medium = TextStyle(
            fontSize = 24.sp,
            lineHeight = 28.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold
        ),
        small = TextStyle(
            fontSize = 24.sp,
            lineHeight = 28.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.SemiBold
        )
    ),

    title = SizedTextStyle(
        large = TextStyle(
            fontSize = 22.sp,
            lineHeight = 26.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Bold
        ),
        medium = TextStyle(
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Medium
        ),
        small = TextStyle(
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Medium
        )
    ),

    body = SizedTextStyle(
        large = TextStyle(
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Normal
        ),
        medium = TextStyle(
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Normal
        ),
        small = TextStyle(
            fontSize = 13.sp,
            lineHeight = 16.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Normal
        )
    ),

    label = SizedTextStyle(
        large = TextStyle(
            fontSize = 14.sp,
            lineHeight = 17.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Medium
        ),
        medium = TextStyle(
            fontSize = 13.sp,
            lineHeight = 16.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Medium
        ),
        small = TextStyle(
            fontSize = 12.sp,
            lineHeight = 15.sp,
            fontFamily = CairoFontFamily,
            fontWeight = FontWeight.Medium
        )
    )
)