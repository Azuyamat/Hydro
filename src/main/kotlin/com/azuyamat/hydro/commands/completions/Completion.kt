package com.azuyamat.hydro.commands.completions

interface Completion {

    fun complete(): List<String>
}