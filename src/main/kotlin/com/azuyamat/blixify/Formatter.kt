package com.azuyamat.blixify

import com.azuyamat.blixify.data.player.getPlayerData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import java.util.UUID

val SMALL_CAPS = mapOf(
    "a" to "ᴀ",
    "b" to "ʙ",
    "c" to "ᴄ",
    "d" to "ᴅ",
    "e" to "ᴇ",
    "f" to "ꜰ",
    "g" to "ɢ",
    "h" to "ʜ",
    "i" to "ɪ",
    "j" to "ᴊ",
    "k" to "ᴋ",
    "l" to "ʟ",
    "m" to "ᴍ",
    "n" to "ɴ",
    "o" to "ᴏ",
    "p" to "ᴘ",
    "q" to "ǫ",
    "r" to "ʀ",
    "s" to "ꜱ",
    "t" to "ᴛ",
    "u" to "ᴜ",
    "v" to "ᴠ",
    "w" to "ᴡ",
    "x" to "x",
    "y" to "ʏ",
    "z" to "ᴢ"
)

object Formatter {

    val mm = MiniMessage.miniMessage()

    private val blixify = smallCaps("BLIXIFY")
    private val prefixComponent = format("<main><bold>$blixify<reset> <dark_gray>» <gray>")

    fun format(string: String, prefix: Boolean = false): Component {
        val message = mm.deserialize(string, chatcolorResolver(), mainColorResolver())
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

    // <main>
    fun mainColorResolver(): TagResolver {
        return TagResolver.resolver(
            "main"
        ) { args: ArgumentQueue, _ ->
            Tag.styling(TextColor.color(140, 140, 255))
        }
    }

    fun sanitize(string: String) = mm.escapeTags(string)

    fun smallCaps(string: String): String {
        var result = ""
        string.forEach {
            result += SMALL_CAPS[it.toString().lowercase()] ?: it
        }
        return result
    }

    fun titleCase(string: String): String {
        var result = ""
        string.forEachIndexed { index, c ->
            val previous = string.getOrNull(index-1)
            result += if (previous == ' ' || previous == null) c.uppercase()
            else c.lowercase()
        }
        return result
    }
}

fun String.parse(prefix: Boolean = false) = Formatter.format(this, prefix)
fun String.sanitize() = Formatter.sanitize(this)
fun String.smallCaps() = Formatter.smallCaps(this)
fun String.titleCase() = Formatter.titleCase(this)