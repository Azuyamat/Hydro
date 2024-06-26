package com.azuyamat.blixify.commands.annotations

annotation class Command(
    val name: String,
    val aliases: Array<String> = [],
    val description: String = "",
    val usage: String = "",
    val permission: String = "",
    val permissionMessage: String = "",
    val cooldown: Long = 0,
)
