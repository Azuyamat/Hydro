package com.azuyamat.blixify.guis.cell

import com.azuyamat.blixify.data.cell.Cell
import com.azuyamat.blixify.data.cell.CellStats
import com.azuyamat.blixify.instance
import com.azuyamat.blixify.helpers.parse
import com.azuyamat.blixify.helpers.titleCase
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

fun statsGUI(player: Player, cell: Cell): GUI {
    return gui(
        instance,
        title = "<gray>Cell stats - ${cell.id}".parse(),
        type = GUIType.Chest(3),
    ) {
        fillBorder {
            item = item(Material.GRAY_STAINED_GLASS_PANE).apply {
                name = Component.empty()
            }
        }

        for (field in CellStats::class.java.declaredFields) {
            field.isAccessible = true
            val value = field.get(cell.stats) as Int
            nextAvailableSlot {
                item = item(Material.PAPER) {
                    name = "<main>${field.name.titleCase()}:".parse()
                    lore = listOf(
                        Component.empty(),
                        "<gray>$value".parse(),
                        Component.empty(),
                    )
                }
            }
        }

        backCellArrow(player)
    }
}