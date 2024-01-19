package com.azuyamat.blixify.scoreboard

import com.azuyamat.blixify.*
import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.helpers.NumberHelper
import com.azuyamat.blixify.helpers.ProgressHelper
import com.azuyamat.blixify.helpers.parse
import com.azuyamat.blixify.helpers.smallCaps
import fr.mrmicky.fastboard.adventure.FastBoard
import org.bukkit.entity.Player

val scoreboardLines = arrayOf(
    "<yellow>",
    "<main><bold>%player_name%",
    "<main>▐ <reset><gray>MINED: <main>%blocks_mined% ⛏",
    "<main>▐ <reset><gray>BALANCE: <main>%balance% <main>⛃",
    "<main>▐ <reset><gray>TOKENS: <main>%tokens% <main>❀",
    "<red>",
    "<main>▐ <reset><gray>EXP: <main>%exp%",
    "<main>▐ <reset><gray>LEVEL: <main>%level%",
    "<main>▐ <reset><gray>PRESTIGE: <main>%prestige%",
    "<red>",
    "<main><bold>SERVER",
    "<main>▐ <reset><gray>PLAYERS: <main>%online_players%<main>/<main>%max_players%",
    "<main>▐ <reset><gray>PING: <main>%ping%",
    "<aqua>"
)

class Scoreboard(private val player: Player) {

    private val board = FastBoard(player)

    init {
        setupBoard()
    }

    private fun setupBoard() {
        board.updateTitle("<main><bold>BLIXIFY <reset><gray>S1".parse())
        updateBoard()
    }

    fun updateBoard() {
        board.updateLines(*scoreboardLines.map {
            val message = toSmallCaps(parseArgs(it))
            message.parse()
        }.toTypedArray())
    }

    private fun parseArgs(string: String): String {
        val data = player.getPlayerData()
        val formatNumber = { number: Number -> NumberHelper(number).toShorten() }
        return string
            .replace("%player_name%", board.player.name)
            .replace("%blocks_mined%", formatNumber(data.stats.blocksMinedNaturally))
            .replace("%balance%", formatNumber(data.vault.balance))
            .replace("%tokens%", formatNumber(data.vault.tokens))
            .replace("%online_players%", instance.server.onlinePlayers.size.toString())
            .replace("%max_players%", instance.server.maxPlayers.toString())
            .replace("%ping%", player.ping.toString()+"ms")
            .replace("%exp%",
                NumberHelper(data.progress.exp)
                    .toBar(
                        ProgressHelper(player).getMaxExp(),
                        20
                    )
            )
            .replace("%level%", formatNumber(data.progress.level))
            .replace("%prestige%", formatNumber(data.progress.prestige))
     }

    // Replace all letters with small caps except if they are in a tag. Ex: <main> and </main> should not be replaced
    private fun toSmallCaps(string: String): String {
        val regex = Regex("(?s)(?<=^|>)[^<>]*(?=<|$)")
        return string.replace(regex) { matchResult -> matchResult.value.smallCaps() }
    }
}