package com.azuyamat.blixify.commands.completions.list

import com.azuyamat.blixify.commands.commands
import com.azuyamat.blixify.commands.completions.Completion

class CommandCompletion : Completion {

    override fun complete(): List<String> {
        return commands.map { it.name }
    }
}