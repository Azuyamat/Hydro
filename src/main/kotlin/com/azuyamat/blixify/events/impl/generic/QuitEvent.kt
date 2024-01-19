package com.azuyamat.blixify.events.impl.generic

import com.azuyamat.blixify.helpers.Logger.info
import com.azuyamat.blixify.data.manipulators.impl.PlayerDataManipulator
import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.helpers.parse
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitEvent : Listener {

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {

        val player = event.player

        // Set quit message
        val quitMessage = "<main>[-] <gray>${player.name} <gray>left the game".parse()
        event.quitMessage(quitMessage)

        // Save player data
        val playerData = player.getPlayerData()
        PlayerDataManipulator.save(playerData)
        PlayerDataManipulator.unCache(player.uniqueId)

        info("Saved player data for ${player.name}")
    }
}