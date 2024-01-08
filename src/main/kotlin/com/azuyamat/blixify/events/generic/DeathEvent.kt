package com.azuyamat.blixify.events.generic

import com.azuyamat.blixify.data.manipulators.PlayerDataManipulator
import com.azuyamat.blixify.data.player.getPlayerData
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class DeathEvent : Listener {

    @EventHandler
    fun onDeath(event: PlayerDeathEvent) {
        val player = event.player

        // Check if cause is a player: if so, increase kills
        val killer = player.killer?.player
        if (killer is Player) {
            val killerData = killer.getPlayerData()

            killerData.stats.kills++
            PlayerDataManipulator.cache(killerData)
        }

        // Increase deaths of victim
        val playerData = player.getPlayerData()
        playerData.stats.deaths++
        PlayerDataManipulator.cache(playerData)
    }
}