package com.azuyamat.blixify.commands.admin

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
        if(player.isFlying){
            player.sendMessage(format("<red>Flight Disabled!"))
            player.allowFlight = !player.isFlying
            return
        } else{
            player.sendMessage(format("<green>Flight Enabled!"))
            player.allowFlight = !player.isFlying
        }
    }
}