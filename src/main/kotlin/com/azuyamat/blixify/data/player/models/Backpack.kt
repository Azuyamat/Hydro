package com.azuyamat.blixify.data.player.models

import org.bukkit.Material

typealias BackpackItem = Material
data class Backpack(
    var size: Int = 64,
    var items: MutableMap<BackpackItem, Int> = mutableMapOf(),
) {

    fun addItem(item: BackpackItem, amount: Int) {
        items[item] = items.getOrDefault(item, 0) + amount
    }

    fun removeItem(item: BackpackItem, amount: Int) {
        items[item] = items.getOrDefault(item, 0) - amount
        if (items[item] == 0) {
            items.remove(item)
        }
    }

    fun clear() {
        items.clear()
    }
}