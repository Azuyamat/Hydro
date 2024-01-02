package com.azuyamat.hydro.data.player

import com.azuyamat.hydro.data.Chatcolor
import com.azuyamat.hydro.data.player.backpack.Backpack
import org.bukkit.entity.Player
import java.io.Serializable
import java.util.UUID

data class PlayerData(
    val uuid: UUID,
    var chatcolor: Chatcolor = Chatcolor.GRAY,
    var stats: PlayerStats = PlayerStats(),
    var backpack: Backpack = Backpack(),
) : Serializable

fun Player.getPlayerData(): PlayerData {
    return PlayerDataManipulator.load(uniqueId)
}