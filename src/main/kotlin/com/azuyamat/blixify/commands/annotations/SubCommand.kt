package com.azuyamat.blixify.commands.annotations

annotation class SubCommand(
    val name: String,
    val description: String = "",
    val usage: String = "/<command>",
    val permission: String = "",
    val permissionMessage: String = "You do not have permission to use this command",
    val isPlayerOnly: Boolean = false,
    val cooldown: Long = 0,
)
