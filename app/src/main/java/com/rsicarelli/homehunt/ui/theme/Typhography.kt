package com.rsicarelli.homehunt.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rsicarelli.homehunt.R

val Eczar = FontFamily(
    Font(R.font.eczar_regular, FontWeight.W400),
    Font(R.font.eczar_medium, FontWeight.W500),
    Font(R.font.eczar_semi_bold, FontWeight.W600),
    Font(R.font.eczar_bold, FontWeight.W700),
    Font(R.font.eczar_extra_bold, FontWeight.W800)
)

val EczarTypography = Typography(
    h1 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W300,
        fontSize = 96.sp
    ),
    h2 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W400,
        fontSize = 60.sp
    ),
    h3 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W600,
        fontSize = 48.sp
    ),
    h4 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W600,
        fontSize = 34.sp
    ),
    h5 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp
    ),
    body1 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W600,
        fontSize = 18.sp
    ),
    body2 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    ),
    button = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp
    ),
    caption = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    overline = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    )
)
