package com.azuyamat.blixify.enchants.impl

import com.azuyamat.blixify.enchants.Enchant
import com.azuyamat.blixify.helpers.ProgressHelper
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import com.azuyamat.blixify.enums.Enchant as EnchantEnum

class Jackpot : Enchant<BlockBreakEvent>(EnchantEnum.JACKPOT, BlockBreakEvent::class.java) {

    override fun onProc(event: BlockBreakEvent, player: Player) {

        val level = getLevel(player).toInt()

        // Calculate how much exp to add
        val expToAdd = (level * 0.0001).toInt()
        ProgressHelper(player).addExp(expToAdd)
    }
}