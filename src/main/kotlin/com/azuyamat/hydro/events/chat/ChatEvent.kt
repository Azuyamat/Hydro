package com.azuyamat.hydro.events.chat

import com.azuyamat.hydro.data.player.getPlayerData
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatEvent : Listener {

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        event.isCancelled = true

        val player = event.player
        val playerData = player.getPlayerData()

        val chatcolor = playerData.chatcolor
        val message = event.originalMessage()

        // Set the format
        val format = Component.text("${player.name}: ").color(NamedTextColor.GRAY)
                .append(
                        message.color(chatcolor.color)
                )

        // Broadcast the message to all viewers separately to allow mods to see uncensored messages
        event.viewers().forEach { viewer ->
            viewer.sendMessage(format)
        }
    }
}