package com.azuyamat.hydro.commands.completions.list

import com.azuyamat.hydro.commands.completions.Completion

class GamemodeCompletion : Completion {

    override fun complete(): List<String> {
        return listOf(
            "survival",
            "creative",
            "adventure",
            "spectator",
            "c",
            "s",
            "a",
            "sp"
        )
    }
}