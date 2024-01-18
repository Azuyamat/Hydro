package com.azuyamat.blixify.commands.impl.cell

import com.azuyamat.blixify.cells.CellPlacer
import com.azuyamat.blixify.cells.Cells
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.commands.annotations.SubCommand
import com.azuyamat.blixify.data.cell.*
import com.azuyamat.blixify.data.manipulators.impl.CellDataManipulator
import com.azuyamat.blixify.data.manipulators.impl.PlayerDataManipulator
import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.enums.CellRank
import com.azuyamat.blixify.guis.cell.cellGUI
import com.azuyamat.blixify.parse
import me.tech.mcchestui.utils.openGUI
import org.bukkit.entity.Player

@Command(
    name = "cell",
    description = "Manage your cell!"
)
class CellCommand {

    fun onCommand(player: Player) {

        val cell = player.getCell()
        if (cell == null) {
            player.sendMessage("<gray>You do not have a cell! Use <main>/cell create<gray> to create one.".parse(true))
            return
        }

        val gui = cellGUI(player, cell)
        player.openGUI(gui)
    }

    @SubCommand("create")
    fun create(player: Player, name: String) {

        // Trim and remove all non-alphanumeric
        val name = name.trim().replace("[^A-Za-z0-9]".toRegex(), "")

        if (CellDataManipulator.cellNameExists(name)) {
            player.sendMessage("<gray>Cell name already exists, try choosing a different name.".parse(true))
            return
        }

        // Make sure nane is not empty
        if (name.isEmpty()) {
            player.sendMessage("<gray>Cell name can't be empty.".parse(true))
            return
        }

        val data = player.getPlayerData()

        if (data.cellId != null) {
            player.sendMessage("<gray>You can't create a cell if you already have one. Either transfer ownership or delete your cell.".parse(true))
            return
        }

        val location = CellPlacer.place()
        val bound = Cells.createCell(location)

        // Save the cell
        val cell = Cell(
            id = name,
            members = mutableMapOf(player.uniqueId to CellRank.OWNER),
            settings = CellSettings(
                location = location
            ),
            stats = CellStats(),
            bound = bound.toCellBoundData()
        )
        CellDataManipulator.save(cell)
        CellDataManipulator.registerNewCellName(name)

        // Set the player's cell to the new cell
        data.cellId = name
        PlayerDataManipulator.save(data)

        // Teleport the player and play the particles
        player.teleport(location)
        bound.highlightDuringTime(y = player.location.blockY)
        player.sendMessage("<gray>Congrats! You've create a cell.".parse(true))
    }

    @SubCommand(
        name = "highlight",
        description = "Highlight your cell's area with cool particles!",
        cooldown = 5000 // 5 seconds
    )
    fun highlight(player: Player) {

        val cell = playerMustHaveCell(player) ?: return
        val bound = cell.bound.toCellBound()

        bound.highlightDuringTime(y = player.location.blockY)

        player.sendMessage("<gray>Your cell's area has been highlighted, look around <main>:)".parse(true))
    }

    @SubCommand(
        name = "expand",
        permission = "blixify.cell.expand",
        description = "Expand your cell's area! (Admin Only)",
    )
    fun expand(player: Player, delta: Int, target: Player?) {

        val target = target?: player

        val cell = playerMustHaveCell(target) ?: return
        val bound = cell.bound.toCellBound()

        bound.highlight(target.location.blockY)

        bound.expand(delta)
        cell.bound.halfDelta = bound.halfDelta

        CellDataManipulator.save(cell)

        player.sendMessage("<main>${target.name}<gray>'s cell has been expanded by <main>$delta<gray> blocks. Please use this command cautiously.".parse(true))

        bound.highlightDuringTime(y = target.location.blockY)
    }

    @SubCommand("shrink")
    fun shrink(player: Player, delta: Int, target: Player?) {

        val target = target?: player

        val cell = playerMustHaveCell(target) ?: return
        val bound = cell.bound.toCellBound()

        bound.highlight(target.location.blockY)

        bound.shrink(delta)
        cell.bound.halfDelta = bound.halfDelta

        CellDataManipulator.save(cell)

        player.sendMessage("<main>${target.name}<gray>'s cell has been shrunk by <main>$delta<gray> blocks. Please use this command cautiously.".parse(true))

        bound.highlightDuringTime(y = target.location.blockY)
    }

    companion object {
        fun playerMustHaveCell(player: Player): Cell? {
            val cell = player.getCell()
            if (cell == null) {
                player.sendMessage("You must have a cell to use this command!") // TODO: Colorful message
                return null
            }
            return cell
        }
    }
}