package com.azuyamat.blixify.commands.completions.impl

import com.azuyamat.blixify.commands.completions.Completion

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