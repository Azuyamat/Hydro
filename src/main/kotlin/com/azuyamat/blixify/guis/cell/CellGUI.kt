package com.azuyamat.blixify.guis.cell

import com.azuyamat.blixify.data.cell.getCell
import com.azuyamat.blixify.data.manipulators.impl.CellDataManipulator
import com.azuyamat.blixify.data.manipulators.impl.PlayerDataManipulator
import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.enums.CellRank
import com.azuyamat.blixify.guis.utility.confirmGUI
import com.azuyamat.blixify.instance
import com.azuyamat.blixify.helpers.parse
import com.azuyamat.blixify.helpers.titleCase
import me.tech.mcchestui.GUI
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.item.headItem
import me.tech.mcchestui.item.item
import me.tech.mcchestui.utils.gui
import me.tech.mcchestui.utils.openGUI
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag

fun cellGUI(player: Player) : GUI {

    val cell = player.getCell() ?: return createCellGUI()
    val rank: () -> CellRank = { cell.members[player.uniqueId] ?: CellRank.NONE }

    return gui(
        plugin = instance,
        title = "<gray>Cell - ${cell.id}".parse(),
        type = GUIType.Chest(3),
    ) {
        fillBorder {
            item = item(Material.GRAY_STAINED_GLASS_PANE).apply {
                name = Component.empty()
            }
        }

        // Player information
        slot(2, 2) {
            item = headItem {
                name = "<main>» <gray>Information".parse()
                lore = listOf(
                    Component.empty(),
                    "<gray>Rank: ".parse().append(Component.text(rank().name.titleCase()).color(rank().defaultColor)),
                    Component.empty(),
                    "<gray>(( Click to teleport ))".parse()
                )
                skullOwner = player
                playerProfile = player.playerProfile
                onClick {
                    player.teleport(cell.settings.location)
                    player.closeInventory()
                }
            }
        }

        // Members
        slot(3, 2) {
            item = item(Material.SPRUCE_SAPLING) {
                name = "<main>» <gray>Members".parse()
                lore = listOf(
                    Component.empty(),
                    "<gray>Members: <main>${cell.members.size}".parse(),
                    Component.empty(),
                    "<gray>(( Click to manage ))".parse()
                )
            }

            onClick {
                val membersGUI = membersGUI(player, cell)
                player.openGUI(membersGUI)
            }
        }

        // Settings
        slot(4, 2) {
            item = item(Material.REDSTONE_TORCH) {
                name = "<main>» <gray>Settings".parse()
                lore = listOf(
                    Component.empty(),
                    "<gray>Settings: ".parse().append(Component.text("TODO")),
                    Component.empty(),
                    "<gray>(( Click to manage ))".parse()
                )
            }
        }

        // Stats
        slot(5, 2) {
            item = item(Material.DIAMOND_SWORD) {
                name = "<main>» <gray>Stats".parse()
                lore = listOf(
                    Component.empty(),
                    "<gray>(( Click to view ))".parse()
                )
                itemMeta.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS)
            }

            onClick {
                val statsGUI = statsGUI(player, cell)
                player.openGUI(statsGUI)
            }
        }

        // Cell bound settings
        slot(6, 2) {
            item = item(Material.BEACON) {
                name = "<main>» <gray>Cell Area".parse()
                lore = listOf(
                    Component.empty(),
                    "<gray>Size: <main>${cell.bound.halfDelta*2}".parse(),
                    Component.empty(),
                    "<gray>(( Click to manage ))".parse()
                )
            }
        }

        // Cell upgrades
        slot(7, 2) {
            item = item(Material.ANVIL) {
                name = "<main>» <gray>Upgrades".parse()
                lore = listOf(
                    Component.empty(),
                    "<gray>(( Click to view ))".parse()
                )
            }

            onClick {
                val upgradesGUI = upgradesGUI(player)
                player.openGUI(upgradesGUI)
            }
        }

        // Quit cell
        if (rank() != CellRank.OWNER) {
            slot(8, 2) {
                item = item(Material.BARRIER) {
                    name = "<main>» <gray>Quit Cell".parse()
                    lore = listOf(
                        Component.empty(),
                        "<gray>(( Click to quit ))".parse()
                    )
                }

                onClick {
                    val confirmGUI = confirmGUI(player, "<red>Quit this cell?".parse()) {
                        val cell = player.getCell() ?: return@confirmGUI
                        cell.members.remove(player.uniqueId)
                        CellDataManipulator.save(cell)
                        player.sendMessage("<gray>You have quit the cell!".parse(true))
                        player.closeInventory()

                        for ((uuid, _) in cell.members) {
                            val localPlayer = instance.server.getPlayer(uuid) ?: continue
                            localPlayer.sendMessage("<main>${player.name} <gray>has quit the cell!".parse(true))
                        }
                    }

                    player.openGUI(confirmGUI)
                }
            }
        } else {
            slot(8, 2) {
                item = item(Material.BARRIER) {
                    name = "<main>» <gray>Delete Cell".parse()
                    lore = listOf(
                        Component.empty(),
                        "<gray>You may transfer ownership".parse(),
                        "<gray>in the members panel".parse(),
                        Component.empty(),
                        "<gray>(( Click to delete ))".parse()
                    )
                }

                onClick {
                    val confirmGUI = confirmGUI(player, "<red>Delete this cell?".parse()) {
                        val cell = player.getCell() ?: return@confirmGUI

                        // Set cell id of all members to null
                        for ((uuid, _) in cell.members) {
                            val localPlayer = instance.server.getPlayer(uuid) ?: continue
                            val data = localPlayer.getPlayerData()
                            data.cellId = null
                            data.save()
                        }

                        val bound = cell.bound.toCellBound()
                        bound.clearBlocks()
                        CellDataManipulator.delete(cell)
                        CellDataManipulator.unregisterCellName(cell.id)
                        player.sendMessage("<red>Cell deleted!".parse())
                        player.closeInventory()
                    }
                    player.openGUI(confirmGUI)
                }
            }
        }
    }
}


// Back arrow
fun GUI.backCellArrow(player: Player) {
    val cell = player.getCell() ?: return
    val rows = bukkitInventory.size / 9
    slot(5, rows) {
        item = item(Material.ARROW) {
            name = "<main>« <gray>Back".parse()
            lore = listOf(
                Component.empty(),
                "<gray>Cell: <main>${cell.id}".parse(),
                Component.empty(),
                "<gray>(( Click to go back ))".parse()
            )
        }

        onClick {
            val cellGUI = cellGUI(player)
            player.closeInventory()
            player.openGUI(cellGUI)
        }
    }
}