package dev.gianmarcodavid.composer

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

object Fonts {
    @Composable
    fun firaCode() = FontFamily(
        Font("FiraCode-Light.ttf", FontWeight.Light),
        Font("FiraCode-Regular.ttf", FontWeight.Normal),
        Font("FiraCode-Medium.ttf", FontWeight.Medium),
        Font("FiraCode-SemiBold.ttf", FontWeight.SemiBold),
        Font("FiraCode-Bold.ttf", FontWeight.Bold),
    )
}