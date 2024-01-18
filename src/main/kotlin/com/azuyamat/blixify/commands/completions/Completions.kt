package com.azuyamat.blixify.commands.completions

import com.azuyamat.blixify.Logger.success
import com.azuyamat.blixify.commandsPackageName
import org.reflections.Reflections

object Completions {

    private val completions = mutableMapOf<String, Completion>()

    fun registerCompletions() {
        val commandsPackage = Reflections("com.azuyamat.blixify.commands.completions.impl")
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