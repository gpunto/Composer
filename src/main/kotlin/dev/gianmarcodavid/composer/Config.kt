package dev.gianmarcodavid.composer

data class Config(
    val maxChars: Int = 280,
    val delimiter: String = "---",
    val ignoreBlankSpace: Boolean = true
)
