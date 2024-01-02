package com.azuyamat.hydro.events.generic

import com.azuyamat.hydro.data.player.PlayerDataManipulator
import com.azuyamat.hydro.data.player.getPlayerData
import com.azuyamat.hydro.instance
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitEvent : Listener {

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {

        val player = event.player
        val playerData = player.getPlayerData()

        // Save player data
        PlayerDataManipulator.save(playerData, true)

        instance.server.logger.info("Saved player data for ${player.name}")
    }
}