package com.azuyamat.hydro.events.generic

import com.azuyamat.hydro.data.manipulators.PlayerDataManipulator
import com.azuyamat.hydro.data.player.getPlayerData
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class DeathEvent : Listener {

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.player
        val playerData = player.getPlayerData()

        // Check if cause is a player
        val killer = player.killer?.player
        if (killer is Player) {
            val killerData = killer.getPlayerData()

            killerData.stats.kills++
            PlayerDataManipulator.cache(killerData)
        }

        playerData.stats.deaths++
        PlayerDataManipulator.cache(playerData)
    }
}