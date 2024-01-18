package com.azuyamat.blixify

import com.azuyamat.blixify.commands.Commands.registerCommands
import com.azuyamat.blixify.commands.completions.Completions.registerCompletions
import com.azuyamat.blixify.data.manipulators.impl.PlayerDataManipulator
import com.azuyamat.blixify.enchants.Enchants.registerEnchants
import com.azuyamat.blixify.events.Events.registerEvents
import com.azuyamat.blixify.gson.typeAdapters.LocationAdapter
import com.azuyamat.blixify.pickaxe.PickaxeManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

const val eventsPackageName = "com.azuyamat.blixify.events.impl"
const val commandsPackageName = "com.azuyamat.blixify.commands.impl"
const val enchantsPackageName = "com.azuyamat.blixify.enchants.impl"

val instance
    get() = JavaPlugin.getPlugin(Blixify::class.java)
var pickaxeManager = PickaxeManager(instance)
val gson: Gson = GsonBuilder()
    .registerTypeAdapter(Location::class.java, LocationAdapter())
    .setPrettyPrinting()
    .create()

class Blixify : JavaPlugin() {
    override fun onEnable() {

        saveResource("pickaxes.yml", false)
        registerManagers()
    }

    override fun onDisable() {

        // Empty player data cache and try to save as much as possible
        for ((uuid, playerData) in PlayerDataManipulator.cache) {
            PlayerDataManipulator.save(playerData)
            PlayerDataManipulator.unCache(uuid)
        }
    }

    fun registerManagers() {

        registerCommands()
        registerEvents()
        registerCompletions()
        registerEnchants()
    }
}
