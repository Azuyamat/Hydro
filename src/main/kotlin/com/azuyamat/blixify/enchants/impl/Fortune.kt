package com.azuyamat.blixify.enchants.impl

import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.enchants.Enchant
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import com.azuyamat.blixify.enums.Enchant as EnchantEnum


class Fortune : Enchant<BlockBreakEvent>(EnchantEnum.FORTUNE, BlockBreakEvent::class.java) {

    override fun onProc(event: BlockBreakEvent, player: Player) {

        val level = getLevel(player).toInt()

        val backpack = player.getPlayerData().backpack
        val amount = (1..level + 1).random()
        val material = event.block.type

        backpack.addItem(material, amount)
    }
}