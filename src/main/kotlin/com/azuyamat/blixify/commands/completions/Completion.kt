package com.azuyamat.blixify.commands.completions

interface Completion {

    fun completeWithArgs(vararg args: String): List<String> = emptyList()

    fun complete(): List<String> = emptyList()
}