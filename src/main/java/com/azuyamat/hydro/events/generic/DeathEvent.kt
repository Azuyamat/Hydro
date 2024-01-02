package com.azuyamat.hydro.events.generic

import com.azuyamat.hydro.data.player.PlayerDataManipulator
import com.azuyamat.hydro.data.player.getPlayerData
import com.azuyamat.hydro.instance
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
            PlayerDataManipulator.save(killerData)
        }

        playerData.stats.deaths++
        PlayerDataManipulator.save(playerData)
    }
}