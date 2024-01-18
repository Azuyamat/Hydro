package com.azuyamat.blixify.commands.impl.admin

import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.commands.annotations.SubCommand
import com.azuyamat.blixify.commands.annotations.Tab
import com.azuyamat.blixify.data.manipulators.impl.PlayerDataManipulator.cache
import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.enums.Enchant as EnchantEnum
import com.azuyamat.blixify.parse
import org.bukkit.entity.Player
import kotlin.math.abs
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
        player.sendMessage("<gray>Use this command to add/remove enchants from players <red>(ADMIN ONLY)".parse(true))
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
        val message = enchants.map { "<main>${it.key.name}<gray>: ${it.value}" }.joinToString("<newline>")

        player.sendMessage("<gray>Enchants for <main>${target.name}<gray>:<newline>$message".parse(true))
    }

    private fun modifyEnchant(target: Player, enchant: String, level: Int) {

        // Check if enchant is valid
        val enchantment = EnchantEnum.entries.find { it.name.equals(enchant, true) } ?: run {
            target.sendMessage("<red>Invalid enchantment!".parse(true))
            return
        }

        val currentLevel = target.getPlayerData().enchants[enchantment] ?: 0
        val newLevel = min(max(currentLevel + level, 0), enchantment.maxLevel)

        val data = target.getPlayerData()
        data.enchants[enchantment] = newLevel
        cache(data)

        val action = if (level > 0) "added" else "removed"
        target.sendMessage("<gray>Your <main>${enchantment.name} <gray>is now at level <main>$newLevel".parse(true))
    }
}