package com.azuyamat.blixify.guis.cell

import com.azuyamat.blixify.data.cell.Cell
import com.azuyamat.blixify.instance
import com.azuyamat.blixify.helpers.parse
import com.azuyamat.blixify.helpers.titleCase
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.headItem
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player

fun membersGUI(player: Player, cell: Cell): GUI {

    return gui(
        plugin = instance,
        title = "<gray>Cell members - ${cell.id}".parse(),
        type = GUIType.Chest(3),
    ) {
        fillBorder {
            item = item(Material.GRAY_STAINED_GLASS_PANE).apply {
                name = Component.empty()
            }
        }

        for ((uuid, rank) in cell.members) {
            val localPlayer = Bukkit.getPlayer(uuid) ?: continue
            nextAvailableSlot {
                item = headItem {
                    name = "<main>${localPlayer.name}".parse()
                    lore = listOf(
                        Component.empty(),
                        "<gray>Rank: ".parse().append(Component.text(rank.name.titleCase()).color(rank.defaultColor)),
                        Component.empty(),
                        "<gray>(( Click to manage ))".parse()
                    )
                    skullOwner = localPlayer
                    glowing = localPlayer == player
                }
            }
        }

        backCellArrow(player)
    }
}