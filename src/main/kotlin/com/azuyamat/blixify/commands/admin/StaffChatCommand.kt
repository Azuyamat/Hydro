package com.azuyamat.blixify.commands.admin

import com.azuyamat.blixify.Formatter.format
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.instance


@Command(
    name = "staffchat",
    description = "Chat to other staff!",
    aliases = ["sc"],
    permission = "blixify.staffchat"
)
class StaffChatCommand {

    fun onCommand(message: String) {

        val players = instance.server.onlinePlayers.filter { it.hasPermission("blixify.staffchat") }
        players.forEach { it.sendMessage(
            format("<red>[Staff] <gray>${it.name}: <white>$message")
        )}
    }
}

