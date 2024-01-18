package com.azuyamat.blixify.cells

import com.azuyamat.blixify.Logger.info
import com.azuyamat.blixify.instance
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.session.ClipboardHolder
import org.bukkit.Location


const val CELL_SIZE = 10

/**
 * Core for creating cells
 */
object Cells {

    fun createCell(location: Location): CellBound {
        info("Creating cell at ${location.x}, ${location.y}, ${location.z}")
        val worldEdit = WorldEdit.getInstance()
        val worldEditWorld = BukkitAdapter.adapt(location.world!!)
        val editSession = worldEdit.newEditSessionBuilder()
            .world(worldEditWorld)
            .build()

        val file = instance.dataFolder.resolve("schematics/cell.schem")

        info("File exists: ${file.exists()}")

        val format = ClipboardFormats.findByFile(file)

        format?.let {
            val reader = it.getReader(file.inputStream())
            val clipboard = reader.read()
            val holder = ClipboardHolder(clipboard)
            val operation = holder.createPaste(editSession)
                .ignoreAirBlocks(false)
                .to(BlockVector3.at(location.x, location.y, location.z))
                .build()

            try {
                Operations.complete(operation)
                editSession.flushQueue()
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

        editSession.close()

        return CellBound(location, CELL_SIZE)
    }
}