package com.azuyamat.blixify.enchants.impl

import com.azuyamat.blixify.enchants.Enchant
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.block.Block
import org.bukkit.entity.FallingBlock
import com.azuyamat.blixify.enums.Enchant as EnchantEnum
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent

class Nuke : Enchant<BlockBreakEvent>(EnchantEnum.FORTUNE, BlockBreakEvent::class.java) {

    // Eventually move this out, so it doesn't only apply Nuke
    val checkLambdas = listOf<(Block) -> Boolean>(
        { block -> block.type.isBlock },
        { block -> block.type !== Material.BEDROCK },
        { block -> block.type.isSolid }
    )

    override fun onProc(event: BlockBreakEvent, player: Player) {

        val level = getLevel(player).toInt()

        // Create a fake explosion of blocks in a radius of 3 around the block
        val radius = -(level/2)..(level/2)
        val center = event.block.location
        for (x in radius) {
            for (y in radius) {
                for (z in radius) {
                    val block = center.clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block

                    // If the block can't be broken, don't break it
                    if (!checkLambdas.all { it(block) }) continue // All lambas must return true

                    block.type = Material.AIR
                    val world = event.block.location.world

                    // Spawn colored dust particles
                    world.spawnParticle(Particle.REDSTONE, block.location, 10, 0.0, 0.0, 0.0, 0.0, DustOptions(Color.BLUE, 1.0f))
                }
            }
        }
    }
}