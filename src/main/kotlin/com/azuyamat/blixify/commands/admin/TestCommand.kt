package com.azuyamat.blixify.commands.admin

import com.azuyamat.blixify.commands.annotations.Catcher
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.commands.annotations.SubCommand
import com.azuyamat.blixify.commands.annotations.Tab
import com.azuyamat.blixify.commands.completions.Completions.getCompletion
import com.azuyamat.blixify.data.manipulators.PlayerDataManipulator
import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.parse
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.entity.Player

@Command(
    name = "test",
    description = "Test command",
    aliases = ["t"],
    permission = "blixify.test"
)
class TestCommand {

    fun onCommand(player: Player): Boolean {
        player.sendMessage("Test command")
        return true
    }

    @SubCommand("completion")
    fun completion(player: Player, @Tab("completion") type: String): Boolean {
        player.sendMessage("Test command completion $type")
        val completions = getCompletion(type)?.complete()?.joinToString(", ")
        player.sendMessage("Completions: $completions")
        return true
    }

    @SubCommand("format")
    fun format(player: Player, @Catcher text: String) {

        val formatted = text.parse(false)
            .hoverEvent(HoverEvent.showText("<gray>Click to copy".parse()))
            .clickEvent(ClickEvent.copyToClipboard(text))

        player.sendMessage(formatted)
    }

    @SubCommand("data")
    fun data(player: Player, target: Player?) {

        val target = target ?: player
        player.sendMessage("Data for ${target.name}".parse(true))

        val data = target.getPlayerData()
        // Get all fields
        val fields = data::class.java.declaredFields
        val message = fields.map { field ->
            field.isAccessible = true
            val value = field.get(data)
            "<blue>${field.name}<gray>: <click:copy_to_clipboard:$value>$value</click>"
        }
        player.sendMessage(message.joinToString("\n").parse())
    }

    @SubCommand("save")
    fun save(player: Player, target: Player?) {

        val target = target ?: player
        player.sendMessage("Saving data for <blue>${target.name}".parse(true))

        val data = target.getPlayerData()
        PlayerDataManipulator.save(data)
    }

    @SubCommand("load")
    fun load(player: Player, target: Player?) {

        val target = target ?: player
        player.sendMessage("Loading data for <blue>${target.name}".parse(true))

        PlayerDataManipulator.load(target.uniqueId)
        player.sendMessage("Loaded data for <blue>${target.name}".parse(true))
    }

    @SubCommand("uncache")
    fun uncache(player: Player, target: Player?) {

        val target = target ?: player
        player.sendMessage("Uncaching data for <blue>${target.name}".parse(true))

        PlayerDataManipulator.unCache(target.uniqueId)
        player.sendMessage("Uncached data for <blue>${target.name}".parse(true))
    }

    @SubCommand("optional")
    fun optional(player: Player, type: String = "nerd") {
        player.sendMessage("Optional $type")
    }
}