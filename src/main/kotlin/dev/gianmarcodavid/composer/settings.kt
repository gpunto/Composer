package dev.gianmarcodavid.composer

import androidx.compose.desktop.AppManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

class Settings {

    var config by mutableStateOf(Config())

    private var openDialog by mutableStateOf(false)

    fun showDialog() {
        openDialog = true
    }

    @Composable
    fun dialog() {
        if (openDialog) {
            var textState by remember { mutableStateOf("") }
            val focusRequester = FocusRequester()

            val location = AppManager.windows.last().window.location.run { IntOffset(x, y) }
            Dialog(
                {
                    textState.toIntOrNull()?.let { config = config.copy(maxChars = it) }
                    openDialog = false
                },
                DialogProperties(centered = false, location = location, title = "Settings")
            ) {
                TextField(
                    value = textState,
                    onValueChange = { textState = it.filter(Char::isDigit) },
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                    modifier = Modifier.focusRequester(focusRequester).fillMaxWidth().padding(10.dp)
                )
            }

            DisposableEffect(Unit) {
                focusRequester.requestFocus()
                onDispose { }
            }
        }
    }
}
