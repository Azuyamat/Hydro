package com.azuyamat.blixify.data.cell

import com.azuyamat.blixify.cells.CellBound
import org.bukkit.Location

data class CellBoundData (
    val center: Location,
    var halfDelta: Int
) {
    fun toCellBound(): CellBound {
        return CellBound(center, halfDelta)
    }
}