package com.azuyamat.blixify.events.impl.cell

import com.azuyamat.blixify.data.cell.getCell
import com.azuyamat.blixify.enums.CellRank
import com.azuyamat.blixify.helpers.parse
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class BlockEvents : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val world = player.world
        if (world.name != "cells") return
        val cell = player.getCell()
        if (cell == null) {
            event.isCancelled = true
            player.sendMessage("<red>You can't break this block. (You do not have a cell)".parse(true))
            return
        }
        val bound = cell.bound.toCellBound()
        if (!bound.contains(event.block.location)) {
            event.isCancelled = true
            player.sendMessage("<red>You can't break this block. (It isn't part of your cell)".parse(true))
        }
        if (cell.members[player.uniqueId]!!.ordinal < CellRank.MEMBER.ordinal) {
            event.isCancelled = true
            player.sendMessage("<red>You can't break this. (You do not have the required rank)".parse(true))
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val world = player.world
        if (world.name != "cells") return
        val cell = player.getCell()
        if (cell == null) {
            event.isCancelled = true
            player.sendMessage("<red>You can't place this here. (You do not have a cell)".parse(true))
            return
        }
        val bound = cell.bound.toCellBound()
        if (!bound.contains(event.block.location)) {
            event.isCancelled = true
            player.sendMessage("<red>You can't place this here. (It isn't part of your cell)".parse(true))
        }
        if (cell.members[player.uniqueId]!!.ordinal < CellRank.MEMBER.ordinal) {
            event.isCancelled = true
            player.sendMessage("<red>You can't place this here. (You do not have the required rank)".parse(true))
        }
    }

    @EventHandler
    fun onInteract(event: PlayerInteractEvent) {
        val player = event.player
        val world = player.world
        if (world.name != "cells") return
        val cell = player.getCell()
        if (cell == null) {
            event.isCancelled = true
            player.sendMessage("<red>You can't interact with this. (You do not have a cell)".parse(true))
            return
        }
        val bound = cell.bound.toCellBound()
        val block = event.clickedBlock ?: return
        if (!bound.contains(block.location)) {
            event.isCancelled = true
            player.sendMessage("<red>You can't interact with this. (It isn't part of your cell)".parse(true))
        }
    }
}