package com.azuyamat.blixify.cells

import com.azuyamat.blixify.data.cell.CellBoundData
import com.azuyamat.blixify.instance
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import kotlin.math.abs

// Bound will always be a square
class CellBound(val center: Location, var halfDelta: Int) {

    var minX = center.blockX - halfDelta
    var maxX = center.blockX + halfDelta
    var minZ = center.blockZ - halfDelta
    var maxZ = center.blockZ + halfDelta

    fun contains(location: Location): Boolean {
        return location.blockX in minX..maxX && location.blockZ in minZ..maxZ
    }

    // Particles around the cell on y level center.blockY
    fun highlight(y: Int = center.blockY) {

        val y = y.toDouble()
        val maxX = maxX + 1
        val maxZ = maxZ + 1

        // Loop over the blocks on the min and max X border
        for (blockZ in minZ..maxZ) {
            spawnParticle(minX.toDouble(), y, blockZ.toDouble())
            spawnParticle(maxX.toDouble(), y, blockZ.toDouble())
        }

        // Loop over the blocks on the min and max Z border
        for (blockX in minX..maxX) {
            spawnParticle(blockX.toDouble(), y, minZ.toDouble())
            spawnParticle(blockX.toDouble(), y, maxZ.toDouble())
        }
    }

    fun highlightDuringTime(time: Long = 10000, y: Int = center.blockY) {

        val start = System.currentTimeMillis()
        val end = start + time

        // Highlight every 1000 milliseconds for time
        object : BukkitRunnable() {
            override fun run() {
                if (System.currentTimeMillis() > end) {
                    cancel()
                    return
                }
                highlight(y)
            }
        }.runTaskTimer(instance, 0, 5L)
    }

    private fun spawnParticle(x: Double, y: Double, z: Double) {
        val location = Location(center.world, x, y, z)

        var (r, g, b) = listOf(
            0.0,
            y,
            0.0
        ).map { abs(it).toInt() % 255 }

        val addWithModulo = { a: Int, b: Int -> (a + b) % 255 }

        for (i in -2..2) {
            val localLocation = location.add(0.0, 1.0, 0.0)
            r = addWithModulo(r, 10)
            g = addWithModulo(g, 10)
            b = addWithModulo(b, 10)
            location.world?.spawnParticle(
                Particle.REDSTONE,
                localLocation,
                10,
                0.0,
                0.0,
                0.0,
                0.0,
                Particle.DustOptions(Color.fromRGB(r, g, b), 1.0f)
            )
        }
    }

    fun expand(delta: Int) {
        halfDelta += delta
        update()
    }

    fun shrink(delta: Int) {
        halfDelta -= delta
        update()
    }

    fun clearBlocks() {
        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                for (y in 0..255) {
                    val location = Location(center.world, x.toDouble(), y.toDouble(), z.toDouble())
                    location.block.type = Material.AIR
                }
            }
        }
    }

    private fun update() {
        minX = center.blockX - halfDelta
        maxX = center.blockX + halfDelta
        minZ = center.blockZ - halfDelta
        maxZ = center.blockZ + halfDelta
    }

    fun toCellBoundData() = CellBoundData(center, halfDelta)
}