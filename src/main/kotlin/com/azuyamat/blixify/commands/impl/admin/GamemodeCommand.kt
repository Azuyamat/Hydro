package com.azuyamat.blixify.commands.impl.admin

import com.azuyamat.blixify.helpers.Formatter.format
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.commands.annotations.Tab
import org.bukkit.GameMode
import org.bukkit.entity.Player

val gamemodes = mapOf(
    "survival" to GameMode.SURVIVAL,
    "spectator" to GameMode.SPECTATOR,
    "creative" to GameMode.CREATIVE,
    "adventure" to GameMode.ADVENTURE,
)

@Command(
    name = "gamemode",
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
        player.sendMessage(format("<gray>Set gamemode to <main>${mode.name} <gray>for <main>${target.name}", true))
        if (target != player) {
            target.sendMessage(format("<gray>Your gamemode was set to <main>${mode.name} <gray>by <main>${player.name}", true))
        }
    }
}