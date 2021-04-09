import androidx.compose.desktop.AppManager
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
import androidx.compose.ui.window.*

private var config by mutableStateOf(280)

fun main() = Window(
    title = "Composer",
    centered = false,
    location = IntOffset(0, 1080),
    menuBar = menuBar()
) {
    var textState by remember { mutableStateOf("") }
    val focusRequester = FocusRequester()

    Box(Modifier.background(Color.White)) {
        TextField(
            value = textState,
            onValueChange = { textState = it },
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

        val charCountFrom = textState.charCountFrom("---")
        Text(
            text = "$charCountFrom/${config}",
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            color = if (charCountFrom > config) Color.Red else Color.Black
        )
    }

    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }
    settingsDialog()
}

private val openDialog = mutableStateOf(false)

private fun menuBar() = MenuBar(
    Menu(
        "File",
        MenuItem("Settings", { openDialog.value = true }, KeyStroke(Key.K))
    )
)

@Composable
private fun settingsDialog() {
    if (openDialog.value) {
        var textState by remember { mutableStateOf("") }
        val focusRequester = FocusRequester()

        val location = AppManager.windows.last().window.location.run { IntOffset(x, y) }
        Dialog(
            {
                textState.toIntOrNull()?.let { config = it }
                openDialog.value = false
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

private fun String.charCountFrom(delimiter: String): Int {
    val delimiterIndex = lastIndexOf(delimiter)
    return if (delimiterIndex < 0) length
    else length - delimiterIndex - delimiter.length
}