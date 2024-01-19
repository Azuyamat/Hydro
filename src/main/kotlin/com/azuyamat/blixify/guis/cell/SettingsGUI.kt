package com.azuyamat.blixify.guis.cell

import com.azuyamat.blixify.data.cell.Cell
import com.azuyamat.blixify.data.manipulators.impl.CellDataManipulator
import com.azuyamat.blixify.instance
import com.azuyamat.blixify.helpers.parse
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

fun settingsGUI(player: Player, cell: Cell): GUI {
    return gui(
        plugin = instance,
        title = "<gray>Cell Settings - ${cell.id}".parse(),
        type = GUIType.Chest(3),
    ) {
        fillBorder {
            item = item(Material.GRAY_STAINED_GLASS_PANE).apply {
                name = Component.empty()
            }
        }

        // Max members
        slot(2, 2) {
            item = item(Material.SPRUCE_SAPLING) {
                name = "<main>» <gray>Max Members".parse()
                lore = listOf(
                    Component.empty(),
                    "<gray>Current: <main>${cell.settings.maxMembers}".parse(),
                    Component.empty(),
                    "<gray>Upgrade your cell to increase".parse(),
                    "<gray>this value".parse(),
                    Component.empty(),
                )
            }
        }

        // Spawn location
        slot(3, 2) {
            item = item(Material.SPRUCE_SAPLING) {
                name = "<main>» <gray>Spawn Location".parse()
                lore = listOf(
                    Component.empty(),
                    "<gray>Current: <main>${cell.settings.location}".parse(),
                    Component.empty(),
                    "<gray>(( Click to change to your location ))".parse()
                )
                onClick {
                    cell.settings.location = player.location
                    CellDataManipulator.save(cell)
                    player.sendMessage("<gray>Cell location updated.".parse(true))
                }
            }
        }
    }
}