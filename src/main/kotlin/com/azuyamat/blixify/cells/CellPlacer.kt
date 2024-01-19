package com.azuyamat.blixify.cells

import com.azuyamat.blixify.data.manipulators.impl.ConfigManipulator
import org.bukkit.Location

const val DELTA_CELL = 100
const val CELLS_PER_SIDE = 10

/**
 * This class defines where cells can be placed in the `cells` world.
 */
object CellPlacer {

    fun place(): Location {

        val currentPlacement = ConfigManipulator.load("main")?.cellPlacementLocation ?: throw NullPointerException("Cell placement location is null!")

        val newPlacement = currentPlacement.clone()

        // Define whether to move on the x or z axis
        val moveOnX = currentPlacement.blockX < (CELLS_PER_SIDE * DELTA_CELL)

        if (moveOnX) {
            newPlacement.add(DELTA_CELL.toDouble(), 0.0, 0.0)
        } else {
            newPlacement.add(0.0, 0.0, DELTA_CELL.toDouble())
            newPlacement.x = 0.0
        }

        ConfigManipulator.save(ConfigManipulator.load("main")!!.copy(cellPlacementLocation = newPlacement))

        return newPlacement
    }
}