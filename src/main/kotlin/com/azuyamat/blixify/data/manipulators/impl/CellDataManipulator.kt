package com.azuyamat.blixify.data.manipulators.impl

import com.azuyamat.blixify.data.cell.Cell
import com.azuyamat.blixify.data.cell.CellId
import com.azuyamat.blixify.data.manipulators.Destination
import com.azuyamat.blixify.data.manipulators.Manipulator
import com.azuyamat.blixify.gson
import com.azuyamat.blixify.instance
import java.io.File

object CellDataManipulator : Manipulator<Cell, CellId> {

    override var cache: MutableMap<CellId, Cell> = mutableMapOf()
    var cellNames: List<String> = getAllCellNames()

    override fun save(data: Cell, destination: Destination) {

        when (destination) {
            Destination.LOCAL -> {
                // Get local file
                val fileName = "${data.id}.json"
                val dataFolder = instance.dataFolder
                val cellFolder = File(dataFolder, "cells")

                // Create cells folder if it doesn't exist
                cellFolder.mkdirs()
                val file = File(cellFolder, fileName)

                // Ensure file exists
                file.parentFile.mkdirs()
                file.createNewFile()

                // Write data to file
                val json = gson.toJson(data)
                file.writeText(json)
            }
            Destination.MONGO -> {
                TODO()
            }
        }

        // Cache data
        cache(data)
    }

    override fun load(uuid: String, destination: Destination): Cell? {

        // If data is cached, return cached data
        if (cache.containsKey(uuid)) {
            return cache[uuid]!! // !! is safe because we checked if it contains the key
        }

        when (destination) {
            Destination.LOCAL -> {
                // Get local file
                val fileName = "$uuid.json"
                val dataFolder = instance.dataFolder
                val cellFolder = File(dataFolder, "cells")
                val file = File(cellFolder, fileName)

                // Ensure file exists
                file.parentFile.mkdirs()
                file.createNewFile()

                // Read data from file
                val json = file.readText()
                val data = gson.fromJson(json, Cell::class.java) ?: return null

                // Cache data
                cache(data)

                return data
            }
            Destination.MONGO -> {
                TODO()
            }
        }
    }

    override fun cache(data: Cell) {
        cache[data.id] = data
    }

    override fun cache(id: String) {
        val data = cache[id] ?: load(id) ?: return
        cache(data)
    }

    override fun unCache(uuid: CellId) {
        cache.remove(uuid)
    }

    fun delete(cell: Cell) {
        val dataFolder = instance.dataFolder
        val cellFolder = File(dataFolder, "cells")
        val file = File(cellFolder, "${cell.id}.json")
        file.delete()
    }

    fun registerNewCellName(name: String) {
        cellNames +=name
    }

    fun unregisterCellName(name: String) {
        cellNames -=name
    }

    fun cellNameExists(name: String): Boolean {
        return cellNames.contains(name.lowercase())
    }

    private fun getAllCellNames(): List<String> {
        val dataFolder = instance.dataFolder
        val cellFolder = File(dataFolder, "cells")
        val files = cellFolder.listFiles() ?: return listOf()
        return files.map { it.nameWithoutExtension }
    }
}