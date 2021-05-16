package dev.gianmarcodavid.composer

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppTheme {
    val colors: Colors = Colors()

    class Colors {
        private val temp = Color.Red

        object Light {
            val background = Color(0xFFFFFFFF)
            val primary = Color(0xFF1098F7)
            val secondary = Color(0xFFFDD325)
            val error = Color(0xFFB00020)
        }

        val material: androidx.compose.material.Colors = lightColors(
            background = Light.background,
            surface = Light.background,
            primary = Light.primary,
            secondary = Light.secondary,
            error = Light.error,

            primaryVariant = Light.primary,
            secondaryVariant = Light.secondary,
        )
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) =
    MaterialTheme(colors = AppTheme.colors.material, content = content)