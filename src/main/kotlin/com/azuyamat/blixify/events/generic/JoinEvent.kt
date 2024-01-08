package com.azuyamat.blixify.events.generic

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinEvent : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {

        val player = event.player

        // Set join message
        val joinMessage =
            Component.text("[+]", NamedTextColor.AQUA)
                .append(Component.text(" ${player.name} joined the game", NamedTextColor.GRAY))
        event.joinMessage(joinMessage)
    }
}