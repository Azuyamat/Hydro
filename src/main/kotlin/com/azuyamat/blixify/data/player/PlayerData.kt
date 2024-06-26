package com.azuyamat.blixify.data.player

import com.azuyamat.blixify.enums.Chatcolor
import com.azuyamat.blixify.enums.Enchant as EnchantEnum
import com.azuyamat.blixify.data.Data
import com.azuyamat.blixify.data.cell.CellId
import com.azuyamat.blixify.data.manipulators.impl.PlayerDataManipulator
import org.bukkit.entity.Player
import java.util.UUID

const val SAVE_INTERVAL = 15 * 60 * 1000L // 15 minutes

data class PlayerData(
    val uuid: UUID,
    var chatcolor: Chatcolor = Chatcolor.GRAY,
    var stats: Stats = Stats(),
    var backpack: Backpack = Backpack(),
    var vault: Vault = Vault(),
    var enchants: MutableMap<EnchantEnum, Long> = EnchantEnum.entries.associateWith { 0L }.toMutableMap(),
    var cellId: CellId? = null,
    var progress: Progress = Progress(),

    @Transient // Will be skipped when saving
    var shouldSave: Long = System.currentTimeMillis() + SAVE_INTERVAL // Default to save passively (if a command is run, it will be saved) every 15 minutes
) : Data {

    fun save() {
        PlayerDataManipulator.save(this)
    }

    fun cache() {
        PlayerDataManipulator.cache(this)
    }
}

fun Player.getPlayerData(): PlayerData {
    return PlayerDataManipulator.load(uniqueId)
}