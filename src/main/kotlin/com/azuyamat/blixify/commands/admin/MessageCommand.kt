package com.azuyamat.blixify.commands.admin

import com.azuyamat.blixify.Formatter.format
import com.azuyamat.blixify.commands.annotations.Catcher
import com.azuyamat.blixify.commands.annotations.Command
import org.bukkit.entity.Player
import java.util.*
import org.bukkit.*

val latestMessages: MutableMap<UUID, UUID> = mutableMapOf()

@Command(
    name = "Message",
    description = "Chat to other players!",
    aliases = ["msg"],
    permission = "blixify.dm"
)


class MessageCommand {

    fun onCommand(player: Player, reciever: Player, @Catcher message: String) {
        reciever.sendMessage(format("<gray>${player.name} §<dark_gray>| §<white>$message"))
        latestMessages.put(player.uniqueId, reciever.uniqueId)
    }
}

@Command(
    name = "Reply",
    description = "Reply to messages!",
    aliases = ["r"],
    permission = "blixify.reply"
)

class ReplyCommand {
    fun onCommand(player: Player, @Catcher message: String) {
        var user = latestMessages.get(player.uniqueId)
        var messager = user?.let { Bukkit.getPlayer(it) }
        if (messager != null) {
            messager.sendMessage(format("<gray>${player.name} §<dark_gray>| §<white>$message"))
        }
    }
}