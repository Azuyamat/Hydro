package com.azuyamat.blixify.scoreboard

import com.azuyamat.blixify.*
import com.azuyamat.blixify.data.player.getPlayerData
import fr.mrmicky.fastboard.adventure.FastBoard
import org.bukkit.entity.Player

val scoreboardLines = arrayOf(
    "<yellow>",
    "<main><bold>PROFILE",
    "<main>▐ <reset><gray>NAME: <main>%player_name%",
    "<main>▐ <reset><gray>RANK: <main>%rank%",
    "<main>▐ <reset><gray>MINED: <main>%blocks_mined% ⛏",
    "<main>▐ <reset><gray>BALANCE: <main>%balance% <main>⛃",
    "<main>▐ <reset><gray>TOKENS: <main>%tokens% <main>❀",
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
        return string
            .replace("%player_name%", board.player.name)
            .replace("%rank%", "Default")
            .replace("%blocks_mined%", data.stats.blocksMined.toString())
            .replace("%balance%", data.vault.balance.toString())
            .replace("%tokens%", data.vault.tokens.toString())
            .replace("%online_players%", instance.server.onlinePlayers.size.toString())
            .replace("%max_players%", instance.server.maxPlayers.toString())
            .replace("%ping%", player.ping.toString()+"ms")
    }

    // Replace all letters with small caps except if they are in a tag. Ex: <main> and </main> should not be replaced
    private fun toSmallCaps(string: String): String {
        val regex = Regex("(?s)(?<=^|>)[^<>]*(?=<|$)")
        return string.replace(regex) { matchResult -> matchResult.value.smallCaps() }
    }
}