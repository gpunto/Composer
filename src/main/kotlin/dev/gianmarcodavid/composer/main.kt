@file:JvmName("MainKt")

package dev.gianmarcodavid.composer

import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.KeyStroke
import androidx.compose.ui.window.Menu
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.MenuItem
import java.util.regex.Pattern

private val settings = Settings()

fun main() = Window(
    title = "Composer",
    centered = false,
    location = IntOffset(0, 1080),
    menuBar = menuBar()
) {
    var textState by remember { mutableStateOf("") }
    val focusRequester = FocusRequester()

    Box(Modifier.background(Color.White)) {
        mainTextArea(
            textState,
            { textState = it },
            focusRequester
        )
        charCounter(
            textState,
            Modifier.align(Alignment.BottomEnd).padding(20.dp)
        )
    }

    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }
    settings.dialog()
}

@Composable
private fun mainTextArea(
    text: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester
) {
    TextField(
        value = text,
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(40.dp)
    )
}

@Composable
private fun charCounter(text: String, modifier: Modifier) {
    val charCountFrom = text.charCountFrom(settings.config.delimiter)
    Text(
        text = "$charCountFrom/${settings.config.maxChars}",
        modifier = modifier,
        color = if (charCountFrom > settings.config.maxChars) Color.Red else Color.Black
    )
}

private fun String.charCountFrom(delimiter: String): Int {
    val delimiterEndIndex = Regex(Pattern.quote(delimiter) + "\\s*")
        .findAll(this)
        .lastOrNull()
        ?.range
        ?.last
        ?: -1

    return length - delimiterEndIndex - 1
}

private fun menuBar() = MenuBar(
    Menu(
        "File",
        MenuItem("Settings", { settings.showDialog() }, KeyStroke(Key.K))
    )
)

