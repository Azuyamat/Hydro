package com.azuyamat.blixify.data.manipulators

import com.azuyamat.blixify.data.player.PlayerData
import com.azuyamat.blixify.instance
import com.azuyamat.blixify.gson
import java.io.File
import java.util.*

object PlayerDataManipulator : Manipulator<PlayerData> {

    override var cache = mutableMapOf<UUID, PlayerData>()

    override fun save(data: PlayerData, destination: Destination) {
        when (destination) {
            Destination.LOCAL -> {
                // Get local file
                val fileName = "${data.uuid}.json"
                val dataFolder = instance.dataFolder
                val file = File(dataFolder, fileName)

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

    override fun load(uuid: UUID, destination: Destination): PlayerData {
        // If data is cached, return cached data
        if (cache.containsKey(uuid)) {
            return cache[uuid]!! // !! is safe because we checked if it contains the key
        }

        // Otherwise, load data from destination
        return when (destination) {
            Destination.LOCAL -> {
                // Get local file
                val fileName = "$uuid.json"
                val dataFolder = instance.dataFolder
                val file = File(dataFolder, fileName)

                // If file doesn't exist, return default data
                if (!file.exists()) {
                    return PlayerData(uuid)
                }

                // Read data from file
                val json = file.readText()
                gson.fromJson(json, PlayerData::class.java)
            }
            Destination.MONGO -> {
                TODO()
            }
        }
    }

    override fun cache(data: PlayerData) {
        cache[data.uuid] = data
    }
}