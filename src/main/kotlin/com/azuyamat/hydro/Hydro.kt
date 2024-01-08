package com.azuyamat.hydro

import com.azuyamat.hydro.Logger.info
import com.azuyamat.hydro.commands.Commands.registerCommands
import com.azuyamat.hydro.commands.completions.Completions.registerCompletions
import com.azuyamat.hydro.data.manipulators.PlayerDataManipulator
import com.azuyamat.hydro.events.Events.registerEvents
import com.azuyamat.hydro.pickaxe.PickaxeManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

const val eventsPackageName = "com.azuyamat.hydro.events"
const val commandsPackageName = "com.azuyamat.hydro.commands"
val instance
    get() = JavaPlugin.getPlugin(Hydro::class.java)
var pickaxeManager = PickaxeManager(instance)
val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

class Hydro : JavaPlugin() {
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
