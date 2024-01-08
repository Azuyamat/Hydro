package com.azuyamat.blixify.data.player

import com.azuyamat.blixify.data.Chatcolor
import com.azuyamat.blixify.data.Data
import com.azuyamat.blixify.data.manipulators.PlayerDataManipulator
import com.azuyamat.blixify.data.player.backpack.Backpack
import com.azuyamat.blixify.data.player.economy.Vault
import org.bukkit.entity.Player
import java.util.UUID

data class PlayerData(
    val uuid: UUID,
    var chatcolor: Chatcolor = Chatcolor.GRAY,
    var stats: PlayerStats = PlayerStats(),
    var backpack: Backpack = Backpack(),
    var vault: Vault = Vault(),
) : Data

fun Player.getPlayerData(): PlayerData {
    return PlayerDataManipulator.load(uniqueId)
}