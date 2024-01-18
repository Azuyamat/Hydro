package com.azuyamat.blixify.data.cell

import org.bukkit.Location

data class CellSettings(
    val maxMembers: Int = 10,
    var location: Location, // Spawn location, center location is specified in bound area
)
