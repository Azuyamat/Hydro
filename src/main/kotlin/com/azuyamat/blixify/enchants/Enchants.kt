package com.azuyamat.blixify.enchants

import com.azuyamat.blixify.Logger.info
import com.azuyamat.blixify.Logger.success
import com.azuyamat.blixify.enchantsPackageName
import com.azuyamat.blixify.instance
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.reflections.Reflections

object Enchants {

    private val enchantEvents: MutableList<Enchant<*>> = mutableListOf()

    fun registerEnchants() {

        val start = System.currentTimeMillis()

        val enchantsPackage = Reflections(enchantsPackageName)
        val enchants = enchantsPackage.getSubTypesOf(Enchant::class.java)

        enchants.forEach(::registerEnchant)

        instance.server.pluginManager.registerEvents(EnchantListener(enchantEvents), instance)

        val end = System.currentTimeMillis()
        val time = end - start

        info("Registered ${enchants.size} enchants in $time ms")
    }

    private fun registerEnchant(enchant: Class<out Enchant<*>>) {

        val instance = enchant.getDeclaredConstructor().newInstance()
        enchantEvents.add(instance)

        success("Registered enchant: ${enchant.simpleName}")
    }
}

class EnchantListener(private val enchants: MutableList<Enchant<*>>) : Listener {

    // Active enchants
    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {

        val triggeredEnchants = enchants
            .filter { it.event == BlockBreakEvent::class.java }
            .mapNotNull { it as? Enchant<BlockBreakEvent> }
        triggeredEnchants.forEach { it.onEvent(event) }
    }

    // Passive enchants
    @EventHandler
    fun onItemSwitch(event: PlayerItemHeldEvent) {

        val triggeredEnchants = enchants
            .filter { it.event == PlayerItemHeldEvent::class.java }
            .mapNotNull { it as? Enchant<PlayerItemHeldEvent> }
        triggeredEnchants.forEach { it.onEvent(event) }
    }
}