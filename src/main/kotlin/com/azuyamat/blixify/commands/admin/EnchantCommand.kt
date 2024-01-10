package com.azuyamat.blixify.commands.admin

import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.commands.annotations.SubCommand
import com.azuyamat.blixify.commands.annotations.Tab
import com.azuyamat.blixify.data.manipulators.PlayerDataManipulator.cache
import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.enums.Enchant
import org.bukkit.entity.Player
import kotlin.math.abs
import kotlin.math.max

@Command(
    name = "enchant",
    description = "Admin enchant command",
    aliases = ["e"],
    permission = "blixify.enchant",
)
class EnchantCommand {

    fun onCommand(player: Player) {
        player.sendMessage("Enchant command <add|remove|list>")
    }

    @SubCommand("add")
    fun add(player: Player, @Tab("enchant") enchant: String, level: Int, target: Player?) {

        modifyEnchant(target ?: player, enchant, abs(level))
    }

    @SubCommand("remove")
    fun remove(player: Player, @Tab("enchant") enchant: String, level: Int, target: Player?) {

        modifyEnchant(target ?: player, enchant, -abs(level))
    }

    @SubCommand("list")
    fun list(player: Player, target: Player?) {

        val target = target ?: player

        val enchants = target.getPlayerData().enchants
        val message = enchants.map { "${it.key.name}: ${it.value}" }.joinToString("\n")

        player.sendMessage(message)
    }

    private fun modifyEnchant(target: Player, enchant: String, level: Int) {

        // Check if enchant is valid
        val enchantment = Enchant.entries.find { it.name.equals(enchant, true) } ?: run {
            target.sendMessage("Invalid enchantment!")
            return
        }

        val currentLevel = target.getPlayerData().enchants[enchantment] ?: 0
        val newLevel = max(currentLevel - level, 0)

        val data = target.getPlayerData()
        data.enchants[enchantment] = newLevel
        cache(data)

        val action = if (level > 0) "Added" else "Removed"
        target.sendMessage("$action $enchantment $level -> ${target.name}")
    }
}