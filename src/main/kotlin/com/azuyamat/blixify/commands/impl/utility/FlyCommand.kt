package com.azuyamat.blixify.commands.impl.utility

import com.azuyamat.blixify.commands.annotations.Command
import org.bukkit.entity.Player
import com.azuyamat.blixify.helpers.parse


@Command(
    name = "fly",
    description = "Fly like a bird",
    aliases = ["flight"],
    permission = "blixify.fly"
)
class FlyCommand {

    fun onCommand(player: Player, target: Player?) {

        val target = target ?: player
        target.allowFlight = !target.allowFlight
        val status = if (target.allowFlight) "enabled" else "disabled"
        val color = if (target.allowFlight) "<green>" else "<red>"
        if (target.isFlying && !target.allowFlight) target.isFlying = false
        else if (!target.isFlying && target.allowFlight) target.isFlying = true
        player.sendMessage("<gray>Flight $color$status <gray>for <main>${target.name}".parse(true))
        if (target != player) {
            target.sendMessage("<gray>Your flight was $color$status <gray>by <main>${player.name}".parse(true))
        }

    }
}