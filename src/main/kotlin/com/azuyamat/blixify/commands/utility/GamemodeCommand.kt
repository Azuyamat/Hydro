package com.azuyamat.blixify.commands.utility

import com.azuyamat.blixify.Formatter.format
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.commands.annotations.Tab
import org.bukkit.GameMode
import org.bukkit.entity.Player

val gamemodes = mapOf(
    "sp" to GameMode.SPECTATOR,
    "s" to GameMode.SURVIVAL,
    "c" to GameMode.CREATIVE,
    "a" to GameMode.ADVENTURE,
)

@Command(
    name = "gm",
    description = "Change your gamemode",
    aliases = ["gm"],
    permission = "blixify.gamemode",
)
class GamemodeCommand {

    fun onCommand(player: Player, @Tab("gamemode") gamemode: String, target: Player?) {

        val mode = gamemodes.filter { it.key.startsWith(gamemode) }.values.firstOrNull() ?: run {
            player.sendMessage(format("<red>Invalid gamemode"))
            return
        }

        val target = target ?: player

        target.gameMode = mode
        player.sendMessage(format("<gray>Set gamemode to <blue>${mode.name} <gray>for <blue>${target.name}"))
        if (target != player) {
            target.sendMessage(format("<gray>Your gamemode was set to <blue>${mode.name} <gray>by <blue>${player.name}"))
        }
    }
}