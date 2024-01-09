package com.azuyamat.blixify.commands

const val PERMISSION_MESSAGE = "<red>[BLIXIFY] You do not have permission to use this command<reset>"

data class CommandInfo(
    val name: String,
    val description: String = "",
    val aliases: List<String> = listOf(),
    val usage: String = "/$name",
    val permission: String = "",
    val permissionMessage: String = PERMISSION_MESSAGE,
    val isPlayerOnly: Boolean = false,
    val subCommands: Map<String, SubCommandInfo> = mutableMapOf()
)

data class SubCommandInfo(
    val name: String,
    val description: String = "",
    val permission: String = "",
    val permissionMessage: String = PERMISSION_MESSAGE,
    val isPlayerOnly: Boolean = false
)
