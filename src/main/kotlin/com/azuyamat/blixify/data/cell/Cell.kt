package com.azuyamat.blixify.data.cell

import org.bukkit.entity.Player

/**
 * A cell can have multiple members
 */
data class Cell(
    val id: String,

    val members: List<Player>,
    val settings: CellSettings,
    val stats: CellStats
)
