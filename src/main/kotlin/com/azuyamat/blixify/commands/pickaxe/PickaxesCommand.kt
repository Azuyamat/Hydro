package com.azuyamat.blixify.commands.pickaxe

import com.azuyamat.blixify.commands.annotations.Tab
import com.azuyamat.blixify.commands.annotations.SubCommand
import com.azuyamat.blixify.pickaxeManager
import org.bukkit.entity.Player
import com.azuyamat.blixify.commands.annotations.Command

@Command(
    name = "pickaxes",
    description = "Pickaxe admin command",
    aliases = ["picks"],
    permission = "blixify.pickaxes",
)
class PickaxesCommand {

    fun onCommand(sender: Player): Boolean {
        sender.sendMessage("Pickaxes command")
        return true
    }

    @SubCommand("list", "List pickaxes")
    fun list(sender: Player): Boolean {
        sender.sendMessage("List pickaxes (player)")
        sender.sendMessage(pickaxeManager.pickaxes.keys.joinToString(", "))
        return true
    }

    @SubCommand("reload", "Reload pickaxe registry")
    fun reload(sender: Player, silent: Boolean = true): Boolean {
        if (!silent)
            sender.sendMessage("Reload pickaxes (player)")
        pickaxeManager.reload()
        return true
    }

    @SubCommand("give", "Give pickaxe to player")
    fun give(player: Player, @Tab("pickaxe") id: String, receiver: Player?): Boolean {
        player.sendMessage("Give pickaxe (player) $id")

        val pickaxe = pickaxeManager.getPickaxe(id) ?: run {
            player.sendMessage("Pickaxe $id not found")
            return true
        }
        val item = pickaxe.create()
        val target = receiver ?: player
        target.inventory.addItem(item)

        return true
    }
}