package com.azuyamat.blixify.commands.admin

import com.azuyamat.blixify.commands.annotations.Catcher
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.commands.annotations.SubCommand
import com.azuyamat.blixify.commands.annotations.Tab
import com.azuyamat.blixify.commands.completions.Completions.getCompletion
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
}