package com.azuyamat.blixify.data.manipulators

import com.azuyamat.blixify.data.player.PlayerData
import com.azuyamat.blixify.data.player.SAVE_INTERVAL
import com.azuyamat.blixify.instance
import com.azuyamat.blixify.gson
import com.azuyamat.blixify.parse
import java.io.File
import java.util.*

object PlayerDataManipulator : Manipulator<PlayerData> {

    override var cache = mutableMapOf<UUID, PlayerData>()

    override fun save(data: PlayerData, destination: Destination) {

        data.shouldSave = System.currentTimeMillis() + SAVE_INTERVAL

        val player = instance.server.getPlayer(data.uuid)
        if (player?.isOnline == true) {
            // If player is online, send action bar
            player.sendActionBar("Your data is being saved".parse(true))
        }

        when (destination) {
            Destination.LOCAL -> {
                // Get local file
                val fileName = "${data.uuid}.json"
                val dataFolder = instance.dataFolder
                val playerFolder = File(dataFolder, "players")
                // Create players folder if it doesn't exist
                playerFolder.mkdirs()
                val file = File(playerFolder, fileName)

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

        val player = instance.server.getPlayer(uuid)
        if (player?.isOnline == true) {
            // If player is online, send action bar
            player.sendActionBar("Your data is being loaded".parse(true))
        }

        // Otherwise, load data from destination
        return when (destination) {
            Destination.LOCAL -> {
                // Get local file
                val fileName = "$uuid.json"
                val dataFolder = instance.dataFolder
                val playersFolder = File(dataFolder, "players")
                // Create players folder if it doesn't exist
                playersFolder.mkdirs()
                val file = File(playersFolder, fileName)

                // If file doesn't exist, return default data
                if (!file.exists()) {
                    return PlayerData(uuid)
                }

                // Read data from file
                val json = file.readText()
                val data = gson.fromJson(json, PlayerData::class.java)
                cache(data)
                data
            }
            Destination.MONGO -> {
                TODO()
            }
        }
    }

    override fun cache(data: PlayerData) {
        cache[data.uuid] = data
    }

    override fun cache(uuid: UUID) {
        val data = cache[uuid] ?: load(uuid)
        cache(data)
    }
}