package com.azuyamat.hydro.events.generic

import com.azuyamat.hydro.Logger.info
import com.azuyamat.hydro.data.manipulators.PlayerDataManipulator
import com.azuyamat.hydro.data.player.getPlayerData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class QuitEvent : Listener {

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {

        val player = event.player

        // Set quit message
        val quitMessage =
            Component.text("[-]", NamedTextColor.GRAY)
                .append(Component.text(" ${player.name} joined the game", NamedTextColor.GRAY))
        event.quitMessage(quitMessage)

        // Save player data
        val playerData = player.getPlayerData()
        PlayerDataManipulator.save(playerData)

        info("Saved player data for ${player.name}")
    }
}