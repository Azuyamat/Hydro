package com.azuyamat.blixify.data.player

import org.bukkit.Material
import kotlin.math.min

typealias BackpackItem = Material
data class Backpack(
    var size: Int = 64,
    var items: MutableMap<BackpackItem, Int> = mutableMapOf(),
) {

    fun addItem(item: BackpackItem, amount: Int) {

        val currentAmountTotal = items.map { it.value }.sum()
        val currentAmountLocal = items.getOrDefault(item, 0)
        val amountToAdd = min(amount, size - currentAmountTotal)
        items[item] = currentAmountLocal + amountToAdd
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
