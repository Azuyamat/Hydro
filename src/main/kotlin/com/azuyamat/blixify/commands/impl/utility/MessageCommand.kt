package com.azuyamat.blixify.commands.impl.utility

import com.azuyamat.blixify.Formatter.format
import com.azuyamat.blixify.commands.annotations.Catcher
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.sanitize
import org.bukkit.entity.Player
import java.util.*
import org.bukkit.*

val latestMessages: MutableMap<UUID, UUID> = mutableMapOf()

@Command(
    name = "message",
    description = "Chat to other players!",
    aliases = ["msg"],
    permission = "blixify.msg"
)
class MessageCommand {

    fun onCommand(player: Player, receiver: Player, @Catcher message: String) {

        interact(player, receiver, message)
    }
}

@Command(
    name = "reply",
    description = "Reply to messages!",
    aliases = ["r"],
    permission = "blixify.reply"
)
class ReplyCommand {

    fun onCommand(player: Player, @Catcher message: String) {

        val receiver = latestMessages[player.uniqueId]?.let { Bukkit.getPlayer(it) } ?: run {
            player.sendMessage(format("<red>There is no one to reply to!"))
            return
        }
        interact(player, receiver, message)
    }
}

fun interact(player: Player, receiver: Player, message: String) {

    latestMessages[player.uniqueId] = receiver.uniqueId
    latestMessages[receiver.uniqueId] = player.uniqueId

    val sanitized = message.sanitize()
    receiver.sendMessage(format(
        "<dark_gray>[<main>✉<dark_gray>] <gray>from <main>${player.name}<gray>: $sanitized"
    ))
    player.sendMessage(format(
        "<dark_gray>[<main>✉<dark_gray>] <gray>to <main>${player.name}<gray>: $sanitized"
    ))
}