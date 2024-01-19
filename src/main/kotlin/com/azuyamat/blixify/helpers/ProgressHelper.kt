package com.azuyamat.blixify.helpers

import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.enums.CustomSound
import net.kyori.adventure.title.Title
import org.bukkit.entity.Player
import kotlin.math.pow

class ProgressHelper(
    private val player: Player,
) {

    fun addExp(amount: Int) {
        val playerData = player.getPlayerData()
        val progress = playerData.progress
        progress.exp += amount

        val maxExp = getMaxExp()
        if (progress.exp >= maxExp) {
            progress.exp -= maxExp
            progress.level += 1

            player.showTitle(Title.title(
                "<main><bold>LEVEL UP!".parse(),
                "<gray>You are now at level <main>${progress.level}".parse()
            ))

            SoundHelper(CustomSound.SUCCESS, player).play()
        }

        playerData.progress = progress
        playerData.save()
    }

    fun getMaxExp(): Int {
        val playerData = player.getPlayerData()
        val progress = playerData.progress
        val level = progress.level

        // Make some calculations here
        val baseExp = 100
        val growthRate = 1.2

        val maxExp = (baseExp * level.toDouble().pow(growthRate)).toInt()

        return maxExp
    }
}