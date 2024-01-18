package com.azuyamat.blixify.enchants

import com.azuyamat.blixify.Logger.info
import com.azuyamat.blixify.data.player.getPlayerData
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerEvent
import java.awt.SystemColor.info
import kotlin.math.max
import com.azuyamat.blixify.enums.Enchant as EnchantEnum

abstract class Enchant<E: Event>(
    val enchant: EnchantEnum,
    val event: Class<E>,
) {

    open fun onEvent(event: E) {

        val player = when(event) {
            is PlayerEvent -> event.player
            is BlockBreakEvent -> event.player
            else -> return
        }

        val enchants = player.getPlayerData().enchants
        val level = enchants[enchant] ?: return
        val willProc = willProc(level, enchant.isPassive, player)

        if (!willProc) return
        if (!matchesSituation(event)) return

        info("Enchant ${enchant.name} procced for ${player.name}")
        onProc(event, player)
    }

    // Override this method to check if the enchant should proc
    open fun matchesSituation(event: E) = true

    // Override this method to handle the enchant proc
    abstract fun onProc(event: E, player: Player)

    private fun calculateChance(level: Long): Double {

        val minChance = enchant.minChance
        val maxChance = enchant.maxChance
        val chanceIncrement = enchant.chanceIncrement

        val currentChance = minChance + (chanceIncrement * (level - 1))

        return max(currentChance, maxChance)
    }

    private fun willProc(level: Long, isPassive: Boolean, player: Player): Boolean {

        if (isPassive) return true
        if (player.isSneaking) return true

        val chance = calculateChance(level)
        val random = Math.random() * 100

        return random <= chance
    }

    fun getLevel(player: Player): Long {
        return player.getPlayerData().enchants[enchant] ?: 0
    }
}