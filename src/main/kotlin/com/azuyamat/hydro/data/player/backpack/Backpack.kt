package com.azuyamat.hydro.data.player.backpack

import org.bukkit.Material

typealias BackpackItem = Material

data class Backpack(
    var size: Int = 64,
    var items: MutableMap<BackpackItem, Int> = mutableMapOf(),
)
