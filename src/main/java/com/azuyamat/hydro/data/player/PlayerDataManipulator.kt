package com.azuyamat.hydro.data.player

import com.azuyamat.hydro.instance
import org.bukkit.configuration.file.YamlConfiguration
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.util.UUID

val playerDataCache = mutableMapOf<UUID, PlayerData>()

object PlayerDataManipulator {

    fun save(data: PlayerData, file: Boolean = false) {
        // Check if the data was modified
        if (playerDataCache[data.uuid] == PlayerData(data.uuid)) {
            return
        }

        // Check if the data should be saved to a file
        if (file) {
            // Get the plugin's data folder
            val dataFolder = instance.dataFolder
            val playerFile = File(dataFolder, "${data.uuid}.yml")
            val playerDataYaml = Yaml()
            val serialized = playerDataYaml.dump(data)

            // Save the data to the file
            playerFile.writeText(serialized)
        }

        // Cache the data
        playerDataCache[data.uuid] = data
    }

    fun load(uuid: UUID): PlayerData {
        // If the data is cached, return the cached data
        if (playerDataCache.containsKey(uuid)) {
            return playerDataCache[uuid]!!
        }

        // Get the plugin's data folder
        val dataFolder = instance.dataFolder
        val playerFile = File(dataFolder, "$uuid.yml")

        // If the file doesn't exist, return empty data
        if (!playerFile.exists()) {
            // Return default data
            return PlayerData(uuid)
        }
        val playerDataYaml = YamlConfiguration()

        // Load the data from the file
        playerDataYaml.load(playerFile)

        // Return the loaded data
        return playerDataYaml as PlayerData
    }
}