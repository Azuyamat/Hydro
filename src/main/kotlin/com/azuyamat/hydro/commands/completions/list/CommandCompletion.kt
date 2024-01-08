package com.azuyamat.hydro.commands.completions.list

import com.azuyamat.hydro.commands.commands
import com.azuyamat.hydro.commands.completions.Completion

class CommandCompletion : Completion {

    override fun complete(): List<String> {
        return commands.map { it.name }
    }
}