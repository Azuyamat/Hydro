package com.azuyamat.hydro

import com.azuyamat.hydro.data.manipulators.PlayerDataManipulator
import com.azuyamat.hydro.events.Events.registerEvents
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bukkit.plugin.java.JavaPlugin

const val eventsPackageName = "com.azuyamat.hydro.events"
val instance
    get() = JavaPlugin.getPlugin(Hydro::class.java)
val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

class Hydro : JavaPlugin() {
    override fun onEnable() {

        // Register events
        registerEvents()
    }

    override fun onDisable() {

        // Empty player data cache and try to save as much as possible
        for ((uuid, playerData) in PlayerDataManipulator.cache) {
            PlayerDataManipulator.save(playerData)
            PlayerDataManipulator.unCache(uuid)
        }
    }
}
