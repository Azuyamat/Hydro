package com.azuyamat.hydro.commands.admin

import com.azuyamat.hydro.commands.annotations.Command
import com.azuyamat.hydro.commands.annotations.SubCommand
import com.azuyamat.hydro.commands.annotations.Tab
import com.azuyamat.hydro.commands.completions.Completions.getCompletion
import com.azuyamat.hydro.instance
import org.bukkit.entity.Player


@Command(
    name = "staffchat",
    description = "Chat to other staff!",
    aliases = ["sc"],
    permission = "blixify.staffchat"
)
class StaffchatCommand {
    fun onCommand(message: String): Boolean {
        val players = instance.server.onlinePlayers.filter { it.hasPermission("blixify.staffchat") }
        players.forEach { it.sendMessage("§b§lSTAFF §8| §f $message") }
        return true
    }
}

