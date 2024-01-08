package com.azuyamat.blixify.commands

import com.azuyamat.blixify.Formatter.format
import com.azuyamat.blixify.commands.annotations.SubCommand
import com.azuyamat.blixify.commands.annotations.Tab
import com.azuyamat.blixify.commands.completions.Completions.getCompletion
import com.azuyamat.blixify.instance
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.valueParameters
import com.azuyamat.blixify.commands.annotations.Command as CommandAnnotation

class CommandBuilder(private val command: KClass<*>) {

    private val info =
        command.findAnnotation<CommandAnnotation>()!! // !! is safe because we check for annotation in Commands.kt

    private val functions = command.functions

    private val mainFunction = functions.firstOrNull { it.name == "onCommand" } ?: run {
        throw Exception("Command ${command.simpleName} does not have a main function")
    }

    private val subFunctions = functions.filter { it.name != "onCommand" }
    private val subCommands = subFunctions.mapNotNull {
        val subInfo = it.findAnnotation<SubCommand>() ?: return@mapNotNull null
        val name = subInfo.name

        val info = SubCommandInfo(
            name = name,
            description = subInfo.description,
            permission = subInfo.permission,
            permissionMessage = subInfo.permissionMessage,
            isPlayerOnly = it.typeParameters.firstOrNull() == Player::class.java,
        )

        name to info
    }.toMap()

    var commandInfo = CommandInfo(
        name = info.name,
        description = info.description,
        aliases = info.aliases.toList(),
        usage = info.usage,
        permission = info.permission,
        permissionMessage = info.permissionMessage,
        subCommands = subCommands,
        isPlayerOnly = mainFunction.typeParameters.firstOrNull() == Player::class.java,
    )

    val playerOnlyMessage = { sender: CommandSender ->
        sender.sendMessage(
            "Only players can use this command"
        )
    }

    val noPermissionMessage = { sender: CommandSender, message: String ->
        sender.sendMessage(
            format(message)
        )
    }

    fun getExecutor(): CommandExecutor {
        return CommandExecutor { sender, _, _, args ->

            // Make sure player has permission
            if (commandInfo.permission.isNotEmpty() && !sender.hasPermission(commandInfo.permission)) {
                noPermissionMessage(sender, commandInfo.permissionMessage)
                return@CommandExecutor true
            }

            val commandInstance = command.constructors.first().call()

            // Main command function
            if (args.isEmpty() || subCommands.isEmpty()) {

                if (commandInfo.isPlayerOnly && sender !is Player) {
                    playerOnlyMessage(sender)
                    return@CommandExecutor true
                }

                val commandSender = if (commandInfo.isPlayerOnly) sender as Player else sender
                val parsedArgs = parseCommandArgs(mainFunction, args)

                val params = mainFunction.valueParameters.slice(1 until mainFunction.valueParameters.size)
                val requiredParams = params.filterNot { it.type.isMarkedNullable }
                val optionalParams = params.filter { it.type.isMarkedNullable }

                if (parsedArgs.filterNotNull().size < requiredParams.size) {
                    sender.sendMessage(
                        format("<red>Usage: /${commandInfo.name} ${requiredParams.joinToString(" ") { "<${it.name}>" }} ${optionalParams.joinToString(" ") { "[${it.name}]" }}" +
                                "\n<gray><> = required, [] = optional")
                    )
                    return@CommandExecutor true
                }

                mainFunction.call(commandInstance, commandSender, *parsedArgs) as? Boolean ?: true
            }
            // Sub command function
            else {

                val subCommand = args[0]
                val subArgs = args.copyOfRange(1, args.size)
                val subFunction = subFunctions.firstOrNull { it.name == subCommand } ?: return@CommandExecutor false

                if (subCommands[subCommand]?.isPlayerOnly == true && sender !is Player) {
                    playerOnlyMessage(sender)
                    return@CommandExecutor true
                }

                if (subCommands[subCommand]?.permission?.isNotEmpty() == true && !sender.hasPermission(subCommands[subCommand]!!.permission)) {
                    noPermissionMessage(sender, subCommands[subCommand]!!.permissionMessage)
                    return@CommandExecutor true
                }

                val commandSender = if (subCommands[subCommand]?.isPlayerOnly == true) sender as Player else sender
                val parsedArgs = parseCommandArgs(subFunction, subArgs)

                val requiredParams = subFunction.valueParameters.slice(1 until subFunction.valueParameters.size).filterNot { it.isOptional }
                val optionalParams = subFunction.valueParameters.slice(1 until subFunction.valueParameters.size).filter { it.isOptional }
                if (parsedArgs.filterNotNull().size < requiredParams.size) {
                    sender.sendMessage(
                        format("<red>Usage: ${commandInfo.usage} $subCommand ${requiredParams.joinToString(" ") { "<${it.name}>" }} ${optionalParams.joinToString(" ") { "[${it.name}]" }}" +
                                "\n<gray><> = required, [] = optional")
                    )
                    return@CommandExecutor true
                }

                subFunction.call(commandInstance, commandSender, *parsedArgs) as Boolean
            }
        }
    }

    private fun parseCommandArgs(method: KFunction<*>, args: Array<String>): Array<*> {

        val parameters = method.valueParameters.slice(1 until method.valueParameters.size)
        val parsedArgs = arrayOfNulls<Any>(parameters.size)

        args.withIndex().forEach { (index, arg) ->
            val parameter = parameters[index]
            val type = parameter.type

            when (type.classifier) {
                Int::class -> parsedArgs[index] = arg.toIntOrNull()
                Double::class -> parsedArgs[index] = arg.toDoubleOrNull()
                Float::class -> parsedArgs[index] = arg.toFloatOrNull()
                Boolean::class -> parsedArgs[index] = arg.toBoolean()
                Player::class -> parsedArgs[index] = instance.server.getPlayer(arg)
                OfflinePlayer::class -> parsedArgs[index] = instance.server.getOfflinePlayer(arg)
                else -> parsedArgs[index] = arg
            }
        }

        return parsedArgs
    }

    fun getTabComplete(): TabCompleter {
        return TabCompleter { sender, _, _, args ->
            if (args.size < 2 && subCommands.isNotEmpty()) {
                // Main function tab complete (sub commands)

                subCommands.keys.toList()
            } else if (subCommands.isNotEmpty()) {
                // Sub function tab complete (sub command args)
                val subCommand = args[0]
                val subFunction = subFunctions.firstOrNull { it.name == subCommand } ?: return@TabCompleter emptyList()
                val parameters = subFunction.valueParameters

                complete(parameters, args.size)
            } else {
                // Main function tab complete (main command args)

                val parameters = mainFunction.valueParameters.slice(1 until mainFunction.valueParameters.size)

                complete(parameters, args.size)
            }
        }
    }

    private fun complete(parameters: List<KParameter>, size: Int): List<String> {
        val index = size - 1
        if (index < 0 || index > parameters.size - 1) return emptyList()

        val parameter = parameters[index]

        val type = parameter.type
        val tabType = parameter.findAnnotation<Tab>()?.list ?: "default"

        return if (tabType == "default") {
            when (type.classifier) {
                Boolean::class -> listOf("true", "false")
                Player::class -> instance.server.onlinePlayers.map { it.name }
                OfflinePlayer::class -> instance.server.offlinePlayers.mapNotNull { it.name }
                else -> emptyList()
            }
        } else {
            getCompletion(tabType)?.complete()
        } ?: emptyList()
    }
}