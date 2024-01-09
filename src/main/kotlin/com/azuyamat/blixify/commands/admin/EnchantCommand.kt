package com.azuyamat.blixify.commands.admin

import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.commands.annotations.SubCommand
import com.azuyamat.blixify.commands.annotations.Tab
import com.azuyamat.blixify.data.manipulators.PlayerDataManipulator.cache
import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.enums.Enchant
import org.bukkit.entity.Player
import kotlin.math.max
import kotlin.math.min

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

        val target = target ?: player

        // Check if enchant is valid
        val enchantment = Enchant.entries.find { it.name.equals(enchant, true) } ?: run {
            player.sendMessage("Invalid enchantment!")
            return
        }

        val currentLevel = target.getPlayerData().enchants[enchantment] ?: 0
        val newLevel = min(currentLevel + level, enchantment.maxLevel)

        val data = target.getPlayerData()
        data.enchants[enchantment] = newLevel
        cache(data)

        player.sendMessage("Added $enchantment $level to ${target.name}")
    }

    @SubCommand("remove")
    fun remove(player: Player, @Tab("enchant") enchant: String, level: Int, target: Player?) {

        val target = target ?: player

        // Check if enchant is valid
        val enchantment = Enchant.entries.find { it.name.equals(enchant, true) } ?: run {
            player.sendMessage("Invalid enchantment!")
            return
        }

        val currentLevel = target.getPlayerData().enchants[enchantment] ?: 0
        val newLevel = max(currentLevel - level, 0)

        val data = target.getPlayerData()
        data.enchants[enchantment] = newLevel
        cache(data)

        player.sendMessage("Removed $enchantment $level from ${target.name}")
    }

    @SubCommand("list")
    fun list(player: Player, target: Player?) {

        val target = target ?: player

        val enchants = target.getPlayerData().enchants
        val message = enchants.map { "${it.key.name}: ${it.value}" }.joinToString("\n")

        player.sendMessage(message)
    }
}