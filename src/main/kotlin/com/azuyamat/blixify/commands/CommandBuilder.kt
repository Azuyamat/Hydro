package com.azuyamat.blixify.commands

import com.azuyamat.blixify.helpers.Formatter.format
import com.azuyamat.blixify.commands.annotations.Catcher
import com.azuyamat.blixify.commands.annotations.SubCommand
import com.azuyamat.blixify.commands.annotations.Tab
import com.azuyamat.blixify.commands.completions.Completions.getCompletion
import com.azuyamat.blixify.data.manipulators.impl.PlayerDataManipulator.save
import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.instance
import com.azuyamat.blixify.commands.impl.utility.generateParameterTooltip
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.math.round
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.valueParameters
import com.azuyamat.blixify.commands.annotations.Command as CommandAnnotation

class CommandBuilder(private val command: KClass<*>) {

    val cooldownManager: MutableMap<UUID, Long> = mutableMapOf()
    val subCommandCooldownManager: MutableMap<UUID, MutableMap<String, Long>> = mutableMapOf()

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
            argumentTooltip = generateParameterTooltip(it.valueParameters.slice(1 until it.valueParameters.size)),
            cooldown = subInfo.cooldown
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
        cooldown = info.cooldown,
        argumentTooltip = generateParameterTooltip(mainFunction.valueParameters.slice(1 until mainFunction.valueParameters.size))
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

            // Make sure player is not on cooldown
            if (commandInfo.cooldown > 0 && sender is Player) {
                val uuid = sender.uniqueId
                val cooldown = commandInfo.cooldown
                val lastSent = cooldownManager[uuid] ?: 0
                val timeSince = System.currentTimeMillis() - lastSent
                val timeLeft = cooldown - timeSince

                val timeLeftSeconds = round(timeLeft / 10.0) / 100.0

                if (timeSince < cooldown) {
                    sender.sendMessage(
                        format(
                            "<gray>You are on cooldown for <main>$timeLeftSeconds seconds", true
                        )
                    )
                    return@CommandExecutor true
                }
            }

            val commandInstance = command.constructors.first().call()

            val function: KFunction<*>
            val commandSender: CommandSender
            val parsedArgs: Array<*>
            var usage = "/${commandInfo.name}"
            val isSubCommand: Boolean

            val requiredParams: List<KParameter>
            val optionalParams: List<KParameter>

            var playerOnly = commandInfo.isPlayerOnly

            // Main command function
            if (args.isEmpty() || subCommands.isEmpty()) {

                isSubCommand = false

                commandSender = if (commandInfo.isPlayerOnly) sender as Player else sender
                parsedArgs = parseCommandArgs(mainFunction, args)

                val params = mainFunction.valueParameters.slice(1 until mainFunction.valueParameters.size)
                requiredParams = params.filterNot { it.type.isMarkedNullable || it.isOptional }
                optionalParams = params.filter { it.type.isMarkedNullable || it.isOptional }

                function = mainFunction
            }
            // Sub command function
            else {
                isSubCommand = true
                val subCommand = args[0]
                val subArgs = args.copyOfRange(1, args.size)
                val subFunction = subFunctions.firstOrNull { it.name == subCommand } ?: return@CommandExecutor false
                val subCommandInfo = subCommands[subCommand] ?: return@CommandExecutor false

                playerOnly = subCommandInfo.isPlayerOnly

                if (subCommandInfo.permission.isNotEmpty() && !sender.hasPermission(subCommandInfo.permission)) {
                    noPermissionMessage(sender, subCommands[subCommand]!!.permissionMessage)
                    return@CommandExecutor true
                }

                // Make sure player is not on cooldown
                if (subCommandInfo.cooldown > 0 && sender is Player) {
                    val uuid = sender.uniqueId
                    val cooldown = subCommandInfo.cooldown
                    val lastSent = subCommandCooldownManager[uuid]?.get(subCommandInfo.name.lowercase()) ?: 0
                    val timeSince = System.currentTimeMillis() - lastSent
                    val timeLeft = cooldown - timeSince

                    val timeLeftSeconds = round(timeLeft / 10.0) / 100.0

                    if (timeSince < cooldown) {
                        sender.sendMessage(
                            format(
                                "<gray>You are on cooldown for <main>$timeLeftSeconds seconds", true
                            )
                        )
                        return@CommandExecutor true
                    }
                }

                commandSender = if (subCommands[subCommand]?.isPlayerOnly == true) sender as Player else sender
                parsedArgs = parseCommandArgs(subFunction, subArgs)

                requiredParams = subFunction.valueParameters.slice(1 until subFunction.valueParameters.size)
                    .filterNot { it.isOptional || it.type.isMarkedNullable }
                optionalParams =
                    subFunction.valueParameters.slice(1 until subFunction.valueParameters.size).filter { it.isOptional || it.type.isMarkedNullable }
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

            val result = function.call(commandInstance, commandSender, *parsedArgs) as? Boolean ?: true

            // Save player data if needed
            if (sender is Player) {
                val playerData = sender.getPlayerData()
                if (playerData.shouldSave < System.currentTimeMillis())
                    save(playerData)
                if (commandInfo.cooldown > 0)
                    cooldownManager[sender.uniqueId] = System.currentTimeMillis()
                if (isSubCommand) {
                    val subName = args.getOrNull(0)?.lowercase() ?: return@CommandExecutor result
                    val subCommandCooldowns = subCommandCooldownManager[sender.uniqueId] ?: mutableMapOf()
                    subCommandCooldowns[subName] = System.currentTimeMillis()
                    subCommandCooldownManager[sender.uniqueId] = subCommandCooldowns
                }
            }

            return@CommandExecutor result
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

            val addToArray = { value: Any? ->
                parsedArgs[index] = value
            }

            when (type.classifier) {
                Int::class -> addToArray(arg.toIntOrNull())
                Double::class -> addToArray(arg.toDoubleOrNull())
                Float::class -> addToArray(arg.toFloatOrNull())
                Boolean::class -> addToArray(arg.toBoolean())
                Player::class -> addToArray(instance.server.getPlayer(arg))
                OfflinePlayer::class -> addToArray(instance.server.getOfflinePlayer(arg))
                else -> addToArray(arg)
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
        val tab = parameter.findAnnotation<Tab>()
        val tabTypeList = tab?.list ?: "default"

        return if (tabTypeList == "default") {
            when (type.classifier) {
                Boolean::class -> listOf("true", "false")
                Player::class -> instance.server.onlinePlayers.map { it.name }
                OfflinePlayer::class -> instance.server.offlinePlayers.mapNotNull { it.name }
                Int::class -> (0..100).map { it.toString() }
                else -> emptyList()
            }
        } else {
            val completion = getCompletion(tabTypeList) ?: return emptyList()

            if (tab?.args?.isEmpty() == true) completion.complete()
            else completion.completeWithArgs(*tab?.args ?: emptyArray())
        }
    }
}