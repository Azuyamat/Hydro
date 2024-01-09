package com.azuyamat.blixify.enchants.list

import com.azuyamat.blixify.enchants.Enchant
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerItemHeldEvent
import com.azuyamat.blixify.enums.Enchant as EnchantEnum

class Efficiency : Enchant<PlayerItemHeldEvent>(EnchantEnum.EFFICIENCY, PlayerItemHeldEvent::class.java) {

    override fun onProc(event: PlayerItemHeldEvent, player: Player) {

        val level = getLevel(player).toInt()
        val item = player.inventory.getItem(event.newSlot) ?: return
        item.addUnsafeEnchantment(Enchantment.DIG_SPEED, level)
        player.inventory.setItem(event.newSlot, item)
    }

    // Make sure player is holding a pickaxe
    override fun matchesSituation(event: PlayerItemHeldEvent): Boolean {

        val player = event.player
        val item = player.inventory.getItem(event.newSlot) ?: return false

        return item.type.name.endsWith("_PICKAXE")
    }
}