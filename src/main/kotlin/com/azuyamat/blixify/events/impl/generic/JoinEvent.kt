package com.azuyamat.blixify.events.impl.generic

import com.azuyamat.blixify.instance
import com.azuyamat.blixify.parse
import com.azuyamat.blixify.scoreboard.Scoreboard
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.scheduler.BukkitRunnable

class JoinEvent : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {

        val player = event.player
        val scoreboardManager = Scoreboard(player)

        // Set join message
        val joinMessage = "<main>[+] <gray>${player.name} <gray>joined the game".parse()
        event.joinMessage(joinMessage)

        // Update scoreboard
        object : BukkitRunnable() {
            override fun run() {
                if (player.isOnline) {
                    scoreboardManager.updateBoard()
                } else {
                    cancel()
                }
            }
        }.runTaskTimer(instance, 0L, 200L)
    }
}