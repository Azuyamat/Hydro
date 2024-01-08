package com.azuyamat.blixify.commands.completions

interface Completion {

    fun complete(): List<String>
}