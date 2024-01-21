package com.azuyamat.blixify.events.impl.generic

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.FoodLevelChangeEvent

class HungerEvent : Listener {

    @EventHandler
    fun onHunger(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }
}