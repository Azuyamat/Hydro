package com.azuyamat.blixify.commands.impl.pickaxe

import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.pickaxeManager
import org.bukkit.entity.Player
import com.azuyamat.blixify.Formatter.format
import com.azuyamat.blixify.parse
import com.azuyamat.blixify.pickaxe.updateLore
import org.bukkit.inventory.ItemFlag

@Command(
    name = "pickaxe",
    description = "Get a free pickaxe!",
    aliases = ["p"],
    cooldown = 5000
)
class PickaxeCommand {

    fun onCommand(player: Player) {

        val pickaxe = pickaxeManager.getPickaxe("default") ?: run {
            player.sendMessage(
                format(
                    "<red>Pickaxe not found! Please contact an administrator."
                )
            )
            return
        }
        val item = pickaxe.create().updateLore(player)
        player.inventory.addItem(item)

        player.sendMessage("<gray>You have received a <main>${pickaxe.id}<gray> pickaxe!".parse(true))
    }
}