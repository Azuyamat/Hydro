package com.azuyamat.hydro.commands.pickaxe

import com.azuyamat.hydro.commands.annotations.Command
import org.bukkit.entity.Player

@Command(
    name = "pickaxe",
    description = "Get a free pickaxe!",
    aliases = ["p"],
)
class PickaxeCommand {

    fun onCommand(player: Player): Boolean {
        player.sendMessage("Pickaxe command")
        return true
    }
}