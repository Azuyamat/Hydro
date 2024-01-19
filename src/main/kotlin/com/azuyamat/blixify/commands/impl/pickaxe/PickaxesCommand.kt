package com.azuyamat.blixify.commands.impl.pickaxe

import com.azuyamat.blixify.commands.annotations.Tab
import com.azuyamat.blixify.commands.annotations.SubCommand
import com.azuyamat.blixify.pickaxeManager
import org.bukkit.entity.Player
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.helpers.parse
import com.azuyamat.blixify.pickaxe.updateLore

@Command(
    name = "pickaxes",
    description = "Pickaxe admin command",
    aliases = ["picks"],
    permission = "blixify.pickaxes",
)
class PickaxesCommand {

    fun onCommand(sender: Player): Boolean {
        sender.sendMessage("<gray>Use this command to manage pickaxes <red>(ADMIN ONLY)".parse(true))
        return true
    }

    @SubCommand("list", "List pickaxes")
    fun list(sender: Player): Boolean {
        sender.sendMessage("<gray>Pickaxes (${pickaxeManager.pickaxes.size}):".parse(true))
        sender.sendMessage(pickaxeManager.pickaxes.map { "<gray>${it.key}" }.joinToString("<gray>, ").parse(true))
        return true
    }

    @SubCommand("reload", "Reload pickaxe registry")
    fun reload(sender: Player): Boolean {
        sender.sendMessage("<gray>Reloading pickaxe registry...".parse(true))
        pickaxeManager.reload()
        sender.sendMessage("<gray>Reloaded pickaxe registry!".parse(true))
        return true
    }

    @SubCommand("give", "Give pickaxe to player")
    fun give(player: Player, @Tab("pickaxe") id: String, receiver: Player?) {
        val pickaxe = pickaxeManager.getPickaxe(id) ?: run {
            player.sendMessage("Pickaxe $id not found")
            return
        }
        val target = receiver ?: player
        val item = pickaxe.create().updateLore(target)
        target.inventory.addItem(item)
        player.sendMessage("<gray>Gave pickaxe <main>$id <gray>to <main>${target.name}".parse(true))
        target.sendMessage("<gray>You have received a <main>${pickaxe.id}<gray> pickaxe!".parse(true))
    }
}