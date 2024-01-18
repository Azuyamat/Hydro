package com.azuyamat.blixify.commands.impl.utility

import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.parse
import org.bukkit.entity.Player

@Command(
    name = "stats",
    description = "Show stats",
    aliases = ["statistics"]
)
class StatsCommand {

    fun onCommand(player: Player, target: Player?) {

        val target = target ?: player
        player.sendMessage("<gray>Stats for <main>${target.name}".parse(true))

        val data = target.getPlayerData()
        val stats = listOf(
            "<gray>---------------------",
            "<gray>UUID: <main>${target.uniqueId}",
            "<gray>Deaths: <main>${data.stats.deaths}",
            "<gray>Kills: <main>${data.stats.kills}",
            "<gray>Mined: <main>${data.stats.blocksMined}",
            "<gray>Balance: <main>${data.vault.balance}",
            "<gray>Tokens: <main>${data.vault.tokens}",
            "<gray>---------------------"
        )

        player.sendMessage(stats.joinToString("<newline>").parse())

    }
}