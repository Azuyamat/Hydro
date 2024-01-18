package com.azuyamat.blixify.guis.cell

import com.azuyamat.blixify.instance
import com.azuyamat.blixify.parse
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import net.kyori.adventure.text.Component
import org.bukkit.Material

fun createCellGUI(): GUI {

    return gui(
        plugin = instance,
        title = "<gray>Create a cell".parse(),
        type = GUIType.Chest(3),
    ) {
        fillBorder {
            item = item(Material.GRAY_STAINED_GLASS_PANE).apply {
                name = Component.empty()
            }
        }

        fill(2, 2, 8, 2) {
            item = item(Material.ORANGE_STAINED_GLASS_PANE).apply {
                name = "<gray>» <main>Create".parse()
                lore = listOf(
                    Component.empty(),
                    "<gray>Run <main>/cell create <name>".parse(),
                    "<gray>to create a cell".parse(),
                    Component.empty(),
                )
            }
        }
    }
}