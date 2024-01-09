package com.azuyamat.blixify.commands.utility

import com.azuyamat.blixify.commands.annotations.Command
import org.bukkit.entity.Player
import com.azuyamat.blixify.Formatter.format


@Command(
    name = "fly",
    description = "Fly like a bird",
    aliases = ["flight"],
    permission = "blixify.fly"
)
class FlyCommand {

    fun onCommand(player: Player) {

        player.allowFlight = !player.allowFlight
        val status = if (player.allowFlight) "enabled" else "disabled"
        val color = if (player.allowFlight) "<green>" else "<red>"
        if (player.isFlying && !player.allowFlight) player.isFlying = false
        else if (!player.isFlying && player.allowFlight) player.isFlying = true
        player.sendMessage(
            format("${color}Flight has been $status")
        )
    }
}