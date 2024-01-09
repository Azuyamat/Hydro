package com.azuyamat.blixify.commands.admin

import com.azuyamat.blixify.Formatter.format
import com.azuyamat.blixify.commands.annotations.Catcher
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.instance
import org.bukkit.entity.Player


@Command(
    name = "staffchat",
    description = "Chat to other staff!",
    aliases = ["sc"],
    permission = "blixify.staffchat"
)
class StaffChatCommand {

    fun onCommand(player: Player, @Catcher message: String) {

        val players = instance.server.onlinePlayers.filter { it.hasPermission("blixify.staffchat") }

        players.forEach { it.sendMessage(
            format("<red>[Staff] <gray>${player.name}: <white>$message")
        )}
    }
}
