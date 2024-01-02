package com.azuyamat.hydro

import com.azuyamat.hydro.data.player.PlayerDataManipulator
import com.azuyamat.hydro.data.player.playerDataCache
import com.azuyamat.hydro.events.Events.registerEvents
import org.bukkit.plugin.java.JavaPlugin

const val eventsPackageName = "com.azuyamat.hydro.events"
val instance
    get() = JavaPlugin.getPlugin(Hydro::class.java)

class Hydro : JavaPlugin() {
    override fun onEnable() {

        // Register events
        registerEvents()
    }

    override fun onDisable() {

        // Empty player data cache
        for ((_, playerData) in playerDataCache) {
            PlayerDataManipulator.save(playerData, true)
        }
    }
}
