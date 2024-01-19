package com.azuyamat.blixify.events.impl.chat

import com.azuyamat.blixify.helpers.Logger.info
import com.azuyamat.blixify.helpers.parse
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import net.kyori.adventure.text.TextComponent
import org.bukkit.entity.Player

class ChatEvent : Listener {

    @EventHandler
    fun onChat(event: AsyncChatEvent) {
        event.isCancelled = true

        val player = event.player
        var message = (event.originalMessage() as TextComponent).content()
        if (message.contains("[item]")) {
            val item = player.inventory.itemInMainHand
            val count = item.amount
            message = message.replace(
                "[item]",
                "<hover:show_text:'<gray>Type: <main>${item.type.name}<newline><gray>Count: <main>$count'><main>${item.type.name}</hover><reset>")
        }

        // Set the format
        val messageFormat = "<gray>${player.name}: <chatcolor:'${player.uniqueId}'>$message"
        val staffFormat = "<click:run_command:/punish ${player.name}><hover:show_text:'<gray>Punish player'><dark_gray>[<red>X<dark_gray>]</hover></click> <click:run_command:/stats ${player.name}><hover:show_text:'<gray>Show stats'><dark_gray>[<main>STATS<dark_gray>]</hover></click> $messageFormat"
        // Broadcast the message to all viewers separately to allow mods to see uncensored messages
        val viewers = event.viewers()
        for (viewer in viewers) {

            val playerViewer = viewer as? Player ?: continue
            val usedFormat = if (playerViewer.hasPermission("blixify.staff")) staffFormat else messageFormat
            playerViewer.sendMessage(usedFormat.parse())
        }

        info("Chat: ${player.name}: $message")
    }
}