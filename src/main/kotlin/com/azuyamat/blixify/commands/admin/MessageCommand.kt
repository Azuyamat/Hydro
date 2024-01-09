package com.azuyamat.blixify.commands.admin

import com.azuyamat.blixify.Formatter.format
import com.azuyamat.blixify.commands.annotations.Catcher
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.commands.annotations.SubCommand
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

    fun onCommand(player: Player, reciever: Player, @Catcher message: String) {

        reciever.sendMessage(format("<gray>${player.name} §<dark_gray>-> §<white>$message"))
        latestMessages[player.uniqueId] = reciever.uniqueId
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
        receiver.sendMessage(format("<gray>${player.name} <dark_gray>-> <white>$message"))
    }

    @SubCommand("Test")
    fun testSubcommand(player: Player){
        player.sendMessage("Test Subcommand")
    }
}
