package com.azuyamat.hydro.commands.admin

import com.azuyamat.hydro.commands.annotations.Command
import com.azuyamat.hydro.commands.annotations.SubCommand
import com.azuyamat.hydro.commands.annotations.Tab
import com.azuyamat.hydro.commands.completions.Completions.getCompletion
import org.bukkit.entity.Player

@Command(
    name = "test",
    description = "Test command",
    aliases = ["t"],
    permission = "hydro.test",
)
class Test {

    fun onCommand(player: Player): Boolean {
        player.sendMessage("Test command")
        return true
    }

    @SubCommand("completion")
    fun completion(player: Player, @Tab("completion") type: String): Boolean {
        player.sendMessage("Test command completion $type")
        val completions = getCompletion(type)?.complete()?.joinToString(", ")
        player.sendMessage("Completions: $completions")
        return true
    }
}