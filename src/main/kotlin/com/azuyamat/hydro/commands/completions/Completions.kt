package com.azuyamat.hydro.commands.completions

import com.azuyamat.hydro.Logger.success
import com.azuyamat.hydro.commandsPackageName
import org.reflections.Reflections

object Completions {

    private val completions = mutableMapOf<String, Completion>()

    fun registerCompletions() {
        val commandsPackage = Reflections("${commandsPackageName}.completions")
        val completions = commandsPackage.getSubTypesOf(Completion::class.java)

        for (completion in completions) {
            val instance = completion.constructors.first().newInstance() as Completion
            val name = completion.simpleName.lowercase().replace("completion", "")
            this.completions[name] = instance
            success("Registered completion ${completion.simpleName}: $name")
        }
    }

    fun getCompletion(name: String): Completion? {
        return completions[name]
    }
}