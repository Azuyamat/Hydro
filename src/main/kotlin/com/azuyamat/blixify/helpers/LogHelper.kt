package com.azuyamat.blixify.helpers

import com.azuyamat.blixify.instance

const val PREFIX = "[BLIXIFY] "


object Logger {

    private val logger = instance.server.logger

    fun info(message: String) {
        wrapper(message, TerminalColor.BLUE)
    }

    fun success(message: String) {
        wrapper(message, TerminalColor.GREEN)
    }

    fun warning(message: String) {
        wrapper(message, TerminalColor.YELLOW)
    }

    fun severe(message: String) {
        wrapper(message, TerminalColor.RED)
    }

    private fun wrapper(message: String, color: TerminalColor) {
        logger.info("${color.color}$PREFIX$message${TerminalColor.RESET.color}")
    }
}

enum class TerminalColor(val color: String) {
    RESET("\u001B[0m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
}