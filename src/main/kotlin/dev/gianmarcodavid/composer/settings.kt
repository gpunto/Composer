package dev.gianmarcodavid.composer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState

class Settings {

    var config by mutableStateOf(Config())

    private var openDialog by mutableStateOf(false)

    fun showDialog() {
        openDialog = true
    }

    @Composable
    fun dialog() {
        if (openDialog) {
            var maxChars by remember { mutableStateOf(config.maxChars.toString()) }
            var delimiter by remember { mutableStateOf(config.delimiter) }
            var ignoreSpace by remember { mutableStateOf(config.ignoreBlankSpace) }

            Dialog(
                onCloseRequest = {
                    updateConfig(maxChars, delimiter, ignoreSpace)
                    openDialog = false
                },
                state = rememberDialogState(position = WindowPosition(Alignment.Center))
            ) {
                Column {
                    maxCharsInput(maxChars) { maxChars = it }
                    delimiterInput(delimiter) { delimiter = it }
                    ignoreSpaceSwitch(ignoreSpace) { ignoreSpace = it }
                }
            }
        }
    }

    private fun updateConfig(maxChars: String, delimiter: String, ignoreBlankSpace: Boolean) {
        config = config.copy(
            maxChars = maxChars.toIntOrNull() ?: config.maxChars,
            delimiter = delimiter.takeUnless(String::isEmpty) ?: config.delimiter,
            ignoreBlankSpace = ignoreBlankSpace
        )
    }

    @Composable
    private fun maxCharsInput(text: String, onValueChange: (String) -> Unit) {
        TextField(
            value = text,
            onValueChange = { onValueChange(it.filter(Char::isDigit)) },
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            maxLines = 1,
            label = { Text("Max characters") }
        )
    }

    @Composable
    private fun delimiterInput(text: String, onValueChange: (String) -> Unit) {
        TextField(
            value = text,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            maxLines = 1,
            label = { Text("Delimiter") }
        )
    }

    @Composable
    private fun ignoreSpaceSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text("Ignore blank space after delimiter")
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}
