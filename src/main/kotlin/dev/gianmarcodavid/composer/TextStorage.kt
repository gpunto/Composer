package dev.gianmarcodavid.composer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import java.io.File

class TextStorage {

    private val folder = File(System.getProperty("user.home"), ".Composer")
    private val file = File(folder, "text.txt")
    private val scope = CoroutineScope(Dispatchers.IO)
    private val flow = MutableSharedFlow<String>()
    private var job: Job? = null

    fun start() {
        file.parentFile.mkdirs()

        job = flow.sample(2000).onEach(file::writeText).launchIn(scope)
    }

    fun stop() {
        job?.cancel()
    }

    fun initial() = file.readText()

    fun store(text: String) = scope.launch { flow.emit(text) }
}
