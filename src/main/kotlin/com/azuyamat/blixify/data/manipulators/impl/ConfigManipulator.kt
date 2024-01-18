package com.azuyamat.blixify.data.manipulators.impl

import com.azuyamat.blixify.data.config.Config
import com.azuyamat.blixify.data.manipulators.Destination
import com.azuyamat.blixify.data.manipulators.Manipulator
import com.azuyamat.blixify.gson
import com.azuyamat.blixify.instance
import java.io.File

object ConfigManipulator: Manipulator<Config, String> {

    override var cache: MutableMap<String, Config> = mutableMapOf()

    override fun save(data: Config, destination: Destination) {

        when (destination) {
            Destination.LOCAL -> {
                // Get local file
                val fileName = "${data.id}-config.json"
                val dataFolder = instance.dataFolder
                val configFolder = File(dataFolder, "configs")

                // Create configs folder if it doesn't exist
                configFolder.mkdirs()
                val file = File(configFolder, fileName)

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

    override fun load(id: String, destination: Destination): Config? {

        // If data is cached, return cached data
        if (cache.containsKey(id)) {
            return cache[id]!! // !! is safe because we checked if it contains the key
        }

        when (destination) {
            Destination.LOCAL -> {
                // Get local file
                val fileName = "$id-config.json"
                val dataFolder = instance.dataFolder
                val configFolder = File(dataFolder, "configs")
                val file = File(configFolder, fileName)

                // Ensure file exists
                if (!file.exists()) {
                    if (id == "main") {
                        // Create default config
                        val config = Config(
                            id = "main",
                            cellPlacementLocation = instance.server.getWorld("cells")!!.spawnLocation
                        )
                        save(config)
                        return config
                    }
                    return null
                }

                // Read data from file
                val json = file.readText()
                val data = gson.fromJson(json, Config::class.java)

                // Cache data
                cache(data)

                return data
            }
            Destination.MONGO -> {
                TODO()
            }
        }
    }

    override fun cache(data: Config) {
        cache[data.id] = data
    }

    override fun cache(id: String) {
        val data = cache[id] ?: load(id) ?: return
        cache(data)
    }

    override fun unCache(uuid: String) {
        cache.remove(uuid)
    }
}