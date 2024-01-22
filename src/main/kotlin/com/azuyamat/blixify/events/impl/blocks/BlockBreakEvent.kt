package com.azuyamat.blixify.events.impl.blocks

import com.azuyamat.blixify.data.player.getPlayerData
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockBreakEvent : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player

        // Make sure block is actually a block and event isn't cancelled
        if (event.isCancelled) return

        val block = event.block
        if (!block.isSolid || block.isLiquid || block.isEmpty) return

        // Increment blocks mined naturally
        val playerData = player.getPlayerData()
        playerData.stats.blocksMinedNaturally++
        playerData.cache()
    }
}