package com.azuyamat.blixify.guis.cell

import com.azuyamat.blixify.instance
import com.azuyamat.blixify.helpers.parse
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

fun upgradesGUI(player: Player): GUI {

    return gui(
        plugin = instance,
        title = "<gray>Cell upgrades".parse(),
        type = GUIType.Chest(3),
    ) {
        fillBorder {
            item = item(Material.GRAY_STAINED_GLASS_PANE).apply {
                name = Component.empty()
            }
        }

        backCellArrow(player)
    }
}