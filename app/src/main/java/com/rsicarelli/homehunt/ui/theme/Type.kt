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

private val RobotoCondensed = FontFamily(
    Font(R.font.roboto_condensed_light, FontWeight.W300),
    Font(R.font.roboto_condensed_regular, FontWeight.W400),
    Font(R.font.roboto_condensed_bold, FontWeight.W700),
)

val EczarTypography = Typography(
    h1 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W400,
        fontSize = 108.sp,
    ),
    h2 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W400,
        fontSize = 68.sp,
    ),
    h3 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W500,
        fontSize = 54.sp,
    ),
    h4 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W500,
        fontSize = 38.sp,
    ),
    h5 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W500,
        fontSize = 30.sp,
    ),
    h6 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W500,
        fontSize = 23.sp,
    ),
    subtitle1 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp,
    ),
    subtitle2 = TextStyle(
        fontFamily = Eczar,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
    ),
    body1 = TextStyle(
        fontFamily = RobotoCondensed,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = RobotoCondensed,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = RobotoCondensed,
        fontWeight = FontWeight.W700,
        fontSize = 14.sp,
    ),
    caption = TextStyle(
        fontFamily = RobotoCondensed,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    ),
    overline = TextStyle(
        fontFamily = RobotoCondensed,
        fontWeight = FontWeight.W400,
        fontSize = 10.sp
    )
)