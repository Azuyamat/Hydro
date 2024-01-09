package com.azuyamat.blixify

import com.azuyamat.blixify.data.player.getPlayerData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import java.util.UUID

object Formatter {

    private val mm = MiniMessage.miniMessage()

    private val prefixComponent = format("<gray>[<blue>BLIXIFY<gray>]<reset> ")

    fun format(string: String, prefix: Boolean = false): Component {
        val message = mm.deserialize(string, chatcolorResolver())
        return if (prefix) prefixComponent.append(message)
        else message
    }

    fun chatcolorResolver(): TagResolver {
        return TagResolver.resolver(
            "chatcolor"
        ) { args: ArgumentQueue, _ ->
            val uuidString = args.popOr("uuid expected").value()
            val uuid = UUID.fromString(uuidString)
            val player = Bukkit.getPlayer(uuid) ?: throw IllegalArgumentException("Player $uuidString not found")

            val color = player.getPlayerData().chatcolor.color
            Tag.styling(TextColor.color(color.red(), color.green(), color.blue()))
        }
    }

    fun sanitize(string: String) = mm.escapeTags(string)
}

fun String.parse(prefix: Boolean = false) = Formatter.format(this, prefix)
fun String.sanitize() = Formatter.sanitize(this)