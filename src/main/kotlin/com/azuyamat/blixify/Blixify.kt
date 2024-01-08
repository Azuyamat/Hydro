package com.azuyamat.blixify

import com.azuyamat.blixify.Logger.info
import com.azuyamat.blixify.commands.Commands.registerCommands
import com.azuyamat.blixify.commands.completions.Completions.registerCompletions
import com.azuyamat.blixify.data.manipulators.PlayerDataManipulator
import com.azuyamat.blixify.events.Events.registerEvents
import com.azuyamat.blixify.pickaxe.PickaxeManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bukkit.plugin.java.JavaPlugin

const val eventsPackageName = "com.azuyamat.blixify.events"
const val commandsPackageName = "com.azuyamat.blixify.commands"
val instance
    get() = JavaPlugin.getPlugin(Blixify::class.java)
var pickaxeManager = PickaxeManager(instance)
val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

class Blixify : JavaPlugin() {
    override fun onEnable() {

        // Save default config if it doesn't exist
        saveResource("pickaxes.yml", false)

        // Register commands
        registerCommands()

        // Register events
        registerEvents()

        // Register completions
        registerCompletions()

        // Print all pickaxes
        info("Pickaxes: ${pickaxeManager.pickaxes.keys.joinToString(", ")}")
    }

    override fun onDisable() {

        // Empty player data cache and try to save as much as possible
        for ((uuid, playerData) in PlayerDataManipulator.cache) {
            PlayerDataManipulator.save(playerData)
            PlayerDataManipulator.unCache(uuid)
        }
    }
}
