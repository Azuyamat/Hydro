package com.azuyamat.blixify.events.chat

import com.azuyamat.blixify.data.player.getPlayerData
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import com.azuyamat.blixify.Formatter.format
import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player

class ChatEvent : Listener {

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        event.isCancelled = true

        val player = event.player
        val playerData = player.getPlayerData()

        val chatcolor = playerData.chatcolor
        val message = (event.originalMessage() as TextComponent).content()

        // Set the format
        val format = format("${player.name}: <chatcolor:'${player.uniqueId}'>$message")
        val staffFormat = format("<click:run_command:/kick ${player.name}>${player.name}</click>: <chatcolor:'${player.uniqueId}'>$message")

        // Broadcast the message to all viewers separately to allow mods to see uncensored messages
        val viewers = event.viewers()
        for (viewer in viewers) {

            val playerViewer = viewer as? Player ?: continue
            val usedFormat = if (playerViewer.hasPermission("blixify.staff")) staffFormat else format
            playerViewer.sendMessage(usedFormat)
        }
    }
}