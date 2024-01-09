package com.azuyamat.blixify.data.player

import com.azuyamat.blixify.enums.Chatcolor
import com.azuyamat.blixify.enums.Enchant as EnchantEnum
import com.azuyamat.blixify.data.Data
import com.azuyamat.blixify.data.manipulators.PlayerDataManipulator
import com.azuyamat.blixify.data.player.models.Backpack
import com.azuyamat.blixify.data.player.models.Vault
import org.bukkit.entity.Player
import java.util.UUID

data class PlayerData(
    val uuid: UUID,
    var chatcolor: Chatcolor = Chatcolor.GRAY,
    var stats: PlayerStats = PlayerStats(),
    var backpack: Backpack = Backpack(),
    var vault: Vault = Vault(),
    var enchants: MutableMap<EnchantEnum, Long> = EnchantEnum.entries.associateWith { 0L }.toMutableMap(),
) : Data

fun Player.getPlayerData(): PlayerData {
    return PlayerDataManipulator.load(uniqueId)
}