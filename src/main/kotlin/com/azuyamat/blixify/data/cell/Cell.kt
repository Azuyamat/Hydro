package com.azuyamat.blixify.data.cell

import com.azuyamat.blixify.data.Data
import com.azuyamat.blixify.data.manipulators.impl.CellDataManipulator
import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.enums.CellRank
import org.bukkit.entity.Player
import java.util.UUID

typealias CellId = String

/**
 * A cell can have multiple members
 */
data class Cell(
    val id: CellId,

    val members: MutableMap<UUID, CellRank>,
    val settings: CellSettings,
    val stats: CellStats,
    val bound: CellBoundData,
) : Data

fun Player.getCell(): Cell? {
    val data = getPlayerData()
    if (data.cellId == null) return null
    return CellDataManipulator.load(data.cellId!!) // !! is safe because we checked if it is null
}
