package com.azuyamat.blixify.commands.completions.list

import com.azuyamat.blixify.commands.completions.Completion

class IntCompletion : Completion {

    override fun complete(): List<String> {
        return (0..100).map { it.toString() }
    }

    override fun completeWithArgs(vararg args: String): List<String> {

        val minimum = args[0].toIntOrNull() ?: 0
        val maximum = args[1].toIntOrNull() ?: 100
        val increment = args[2].toIntOrNull() ?: 1

        // Return a list of numbers from minimum to maximum, incrementing by increment
        return (minimum..maximum step increment).map { it.toString() }
    }
}