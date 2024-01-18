package com.azuyamat.blixify.data.config

import com.azuyamat.blixify.data.Data
import org.bukkit.Location

data class Config(
    val id: String = "main",
    val cellPlacementLocation: Location
): Data
