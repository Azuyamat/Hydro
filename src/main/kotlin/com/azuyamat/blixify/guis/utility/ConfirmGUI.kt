package com.azuyamat.blixify.guis.utility

import com.azuyamat.blixify.instance
import com.azuyamat.blixify.helpers.parse
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

fun confirmGUI(player: Player, title: Component, onConfirm: () -> Unit): GUI {
    return gui(
        plugin = instance,
        title = title,
        type = GUIType.Chest(3),
    ) {
        fill(1, 1, 9, 3) {
            item = item(Material.GRAY_STAINED_GLASS_PANE).apply {
                name = Component.empty()
            }
        }
        slot(4, 2) {
            item = item(Material.GREEN_STAINED_GLASS_PANE) {
                name = "<green>Confirm".parse()
            }
            onClick {
                onConfirm()
                player.closeInventory()
            }
        }
        slot(6, 2) {
            item = item(Material.RED_STAINED_GLASS_PANE) {
                name = "<red>Cancel".parse()
            }
            onClick {
                player.closeInventory()
                player.sendMessage("<red>Cancelled operation".parse())
            }
        }
    }
}