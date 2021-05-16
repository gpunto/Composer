@file:JvmName("MainKt")

package dev.gianmarcodavid.composer

import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.KeyStroke
import androidx.compose.ui.window.Menu
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.MenuItem
import java.util.regex.Pattern

private val settings = Settings()

private val textStorage = TextStorage().also(TextStorage::start)

fun main() = Window(
    title = "Composer",
    centered = true,
    menuBar = menuBar(),
) {
    AppTheme {
        var textState by remember { mutableStateOf(textStorage.initial()) }

        val focusRequester = FocusRequester()

        Box(Modifier.background(Color.White).fillMaxWidth()) {
            Box(modifier = Modifier.widthIn(max = 800.dp).fillMaxWidth().align(Alignment.TopCenter)) {
                mainTextArea(
                    textState,
                    { textState = it; textStorage.store(it) },
                    focusRequester
                )
                charCounter(
                    textState,
                    Modifier.align(Alignment.BottomEnd).padding(20.dp)
                )
            }
        }

        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose { }
        }
        settings.dialog()
    }
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
        textStyle = LocalTextStyle.current.copy(fontFamily = Fonts.firaCode(), fontSize = 16.sp),
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(40.dp)
    )
}

@Composable
private fun charCounter(text: String, modifier: Modifier) {
    val charCountFrom = text.charCountFrom(settings.config.delimiter, settings.config.ignoreBlankSpace)
    Text(
        text = "$charCountFrom/${settings.config.maxChars}",
        modifier = modifier,
        color = if (charCountFrom > settings.config.maxChars) Color.Red else Color.Black
    )
}

private fun String.charCountFrom(delimiter: String, ignoreBlankSpace: Boolean): Int {
    val delimiterEndIndex =
        if (ignoreBlankSpace) ignoringBlankSpaceIndex(delimiter)
        else withBlankSpaceIndex(delimiter)

    return length - delimiterEndIndex - 1
}

private fun String.ignoringBlankSpaceIndex(delimiter: String) =
    Regex(Pattern.quote(delimiter) + "\\s*")
        .findAll(this)
        .lastOrNull()
        ?.range
        ?.last
        ?: -1

private fun String.withBlankSpaceIndex(delimiter: String): Int {
    val index = lastIndexOf(delimiter)
    return if (index >= 0) index + delimiter.length - 1 else -1
}

private fun menuBar() = MenuBar(
    Menu(
        "File",
        MenuItem("Settings", { settings.showDialog() }, KeyStroke(Key.K))
    )
)

