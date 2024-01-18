package com.azuyamat.blixify.commands.impl.utility

import com.azuyamat.blixify.Formatter.format
import com.azuyamat.blixify.commands.annotations.Command
import com.azuyamat.blixify.commands.annotations.Tab
import com.azuyamat.blixify.commands.commands
import org.bukkit.entity.Player
import kotlin.reflect.KParameter

const val COMMANDS_PER_PAGE = 5

@Command(
    name = "help",
    description = "Help command",
    aliases = ["h", "?"],
)
class HelpCommand {

    fun onCommand(player: Player, @Tab("command") command: String?): Boolean {

        val isCommand = command != null && command.toIntOrNull() == null
        val allowedCommands = commands.filter { it.permission.isNotEmpty() && player.hasPermission(it.permission) || it.permission.isEmpty() }

        val page = command?.toIntOrNull() ?: 1
        val commandsSize = allowedCommands.size

        val maxPage = (commandsSize + COMMANDS_PER_PAGE - 1) / COMMANDS_PER_PAGE

        val headerInfo = if (isCommand) "$command" else "$page/$maxPage"

        val previousPageButton = if (page > 1 && !isCommand) "<click:run_command:/help ${page - 1}><hover:show_text:'<gray>Previous page'><main>[<]</hover></click>" else ""
        val nextPageButton = if (page < maxPage && !isCommand) "<click:run_command:/help ${page + 1}><hover:show_text:'<gray>Next page'><main>[>]</hover></click>" else ""

        player.sendMessage(
            format("<gray>-----$previousPageButton----<main>Help ($headerInfo)<gray>-----$nextPageButton----")
        )

        if (isCommand) {
            val commandInfo = allowedCommands.find { it.name.equals(command, true) } ?: run {
                player.sendMessage(format("<red>Command not found"))
                return true
            }

            val name = commandInfo.name
            val description = commandInfo.description.takeIf { it.isNotEmpty() } ?: "No description"
            val usage = commandInfo.usage.takeIf { it.isNotEmpty() } ?: "/$name"
            val permission = commandInfo.permission.takeIf { it.isNotEmpty() } ?: "None"

            player.sendMessage(
                format("<hover:show_text:'${commandInfo.argumentTooltip}'><${if (permission != "None") "red" else "main"}>/$name</hover><gray>: $description\n" +
                        "<gray>Usage: $usage\n" +
                        "<gray>Permission: $permission\n" +
                        "<gray>Cooldown: ${commandInfo.cooldown} seconds")
            )

            val subCommands = commandInfo.subCommands.filter {it.value.permission.isNotEmpty() || player.hasPermission(it.value.permission)}
            val subCommandsList = subCommands.map { subCommand ->
                val subName = subCommand.value.name
                val subDescription = subCommand.value.description.takeIf { it.isNotEmpty() } ?: "No description"
                val requiresPermission = subCommand.value.permission.isNotEmpty() || commandInfo.permission.isNotEmpty()

                format("<hover:show_text:'${subCommand.value.argumentTooltip}'><${if (requiresPermission) "red" else "main"}>/$name $subName</hover><gray>: $subDescription")
            }
            subCommandsList.forEach { player.sendMessage(it) }

            return true
        }

        val startIndex = (page - 1) * COMMANDS_PER_PAGE
        val endIndex = startIndex + COMMANDS_PER_PAGE

        val shownCommands = allowedCommands.subList(startIndex, endIndex.coerceAtMost(commandsSize))
        val shownCommandsList = shownCommands.map { command ->
            val name = command.name
            val description = command.description
            val requiresPermission = command.permission.isNotEmpty()

            "<hover:show_text:'<gray>Click for more help'><click:run_command:/help $name><${if (requiresPermission) "red" else "main"}>/$name<gray>: $description</click></hover>"
        }

        player.sendMessage(format(shownCommandsList.joinToString("\n")))

        return true
    }
}

fun generateParameterTooltip(parameters: List<KParameter>): String {
    return parameters.joinToString("<newline>") {
        val name = if (it.type.isMarkedNullable) "[${it.name}]" else "<${it.name}>" // Optional parameters are surrounded by []
        val type = it.type.classifier.toString().split(".").last()
        "<gray>$name : <main>$type"
    }
}