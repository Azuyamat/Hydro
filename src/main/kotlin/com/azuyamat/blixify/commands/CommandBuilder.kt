package com.azuyamat.blixify.commands

import com.azuyamat.blixify.Formatter.format
import com.azuyamat.blixify.Logger.info
import com.azuyamat.blixify.commands.annotations.Catcher
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
import kotlin.reflect.full.hasAnnotation
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

    val usageMessage = { sender: CommandSender, usage: String ->
        sender.sendMessage(
            format(
                "<red>Usage: $usage" +
                        "\n<gray><> = required, [] = optional"
            )
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

            val function: KFunction<*>
            val commandSender: CommandSender
            val parsedArgs: Array<*>
            var usage = "/${commandInfo.name}"

            val requiredParams: List<KParameter>
            val optionalParams: List<KParameter>

            var playerOnly = commandInfo.isPlayerOnly

            // Main command function
            if (args.isEmpty() || subCommands.isEmpty()) {

                commandSender = if (commandInfo.isPlayerOnly) sender as Player else sender
                parsedArgs = parseCommandArgs(mainFunction, args)

                val params = mainFunction.valueParameters.slice(1 until mainFunction.valueParameters.size)
                requiredParams = params.filterNot { it.type.isMarkedNullable }
                optionalParams = params.filter { it.type.isMarkedNullable }

                function = mainFunction
            }
            // Sub command function
            else {

                val subCommand = args[0]
                val subArgs = args.copyOfRange(1, args.size)
                val subFunction = subFunctions.firstOrNull { it.name == subCommand } ?: return@CommandExecutor false

                playerOnly = subCommands[subCommand]?.isPlayerOnly ?: playerOnly

                if (subCommands[subCommand]!!.permission.isNotEmpty() && !sender.hasPermission(subCommands[subCommand]!!.permission)) {
                    noPermissionMessage(sender, subCommands[subCommand]!!.permissionMessage)
                    return@CommandExecutor true
                }

                commandSender = if (subCommands[subCommand]?.isPlayerOnly == true) sender as Player else sender
                parsedArgs = parseCommandArgs(subFunction, subArgs)

                requiredParams = subFunction.valueParameters.slice(1 until subFunction.valueParameters.size)
                    .filterNot { it.isOptional }
                optionalParams =
                    subFunction.valueParameters.slice(1 until subFunction.valueParameters.size).filter { it.isOptional }
                usage += " $subCommand"

                function = subFunction
            }


            if (playerOnly && sender !is Player) {
                playerOnlyMessage(sender)
                return@CommandExecutor true
            }

            usage += " ${requiredParams.joinToString(" ") { "<${it.name}>" }} ${optionalParams.joinToString(" ") { "[${it.name}]" }}"

            // Not enough args given
            if (parsedArgs.filterNotNull().size < requiredParams.size) {
                usageMessage(sender, usage)
                return@CommandExecutor true
            }

            function.call(commandInstance, commandSender, *parsedArgs) as? Boolean ?: true
        }
    }

    private fun parseCommandArgs(method: KFunction<*>, args: Array<String>): Array<*> {

        val parameters = method.valueParameters.slice(1 until method.valueParameters.size)
        val parsedArgs = arrayOfNulls<Any>(parameters.size)

        for ((index, arg) in args.withIndex()) {

            val parameter = parameters.getOrNull(index) ?: break
            val type = parameter.type

            if (parameter.hasAnnotation<Catcher>()) {
                parsedArgs[index] = args.slice(index until args.size).joinToString(" ")
                break
            }

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