package com.azuyamat.blixify.commands

import com.azuyamat.blixify.Logger.info
import com.azuyamat.blixify.Logger.success
import com.azuyamat.blixify.commandsPackageName
import com.azuyamat.blixify.instance
import com.azuyamat.blixify.commands.annotations.Command
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandMap
import org.bukkit.command.PluginCommand
import org.reflections.Reflections
import kotlin.reflect.KClass

val commands: MutableList<CommandInfo> = mutableListOf()

object Commands {

    fun registerCommands() {

        val start = System.currentTimeMillis()

        val commandsPackage = Reflections(commandsPackageName)
        // Get all classes with Command annotation
        val commands = commandsPackage.getTypesAnnotatedWith(Command::class.java)
        val commandMap = getCommandMap()

        for (command in commands) {
            val builder = registerCommand(command.kotlin) ?: continue
            commandMap.register(
                builder.label,
                builder
            )
        }

        val end = System.currentTimeMillis()
        val time = end - start

        info("Registered ${commands.size} commands in $time ms")
    }

    private fun registerCommand(command: KClass<*>): PluginCommand? {

        val parsedCommand = CommandBuilder(command)
        val info = parsedCommand.commandInfo

        val builderConstructor = PluginCommand::class.java.declaredConstructors.first()
        builderConstructor.isAccessible = true
        val builder = builderConstructor.newInstance(info.name, instance) as PluginCommand

        builder.apply {
            aliases = info.aliases.toList()
            description = info.description
            usage = info.usage.ifEmpty { "/$name" }
            permission = info.permission
            label = name
            permissionMessage(Component.text(info.permissionMessage))
            setExecutor(parsedCommand.getExecutor())
            tabCompleter = parsedCommand.getTabComplete()
        }

        commands.add(info)
        success("Registered command: ${info.name} (${info.aliases.joinToString(", ")})")

        return builder
    }

    private fun getCommandMap(): CommandMap {
        val getCommandMap = instance.server.javaClass.getDeclaredMethod("getCommandMap")
        getCommandMap.isAccessible = true
        return getCommandMap.invoke(instance.server) as CommandMap

    }
}