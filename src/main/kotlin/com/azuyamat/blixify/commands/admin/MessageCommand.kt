package com.azuyamat.blixify.commands.admin

import com.azuyamat.blixify.Formatter.format
import com.azuyamat.blixify.commands.annotations.Catcher
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.commands.annotations.SubCommand
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
        latestMessages[player.uniqueId] = receiver.uniqueId
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

    val sanitized = message.sanitize()
    receiver.sendMessage(format(
        "<dark_gray>[<blue><bold>✉<reset><dark_gray>] <gray>from <blue>${player.name}<gray>: $sanitized"
    ))
    player.sendMessage(format(
        "<dark_gray>[<blue><bold>✉<reset><dark_gray>] <gray>to <blue>${player.name}<gray>: $sanitized"
    ))
}