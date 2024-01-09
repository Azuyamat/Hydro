package com.azuyamat.blixify.scheduler

import org.bukkit.entity.Player

data class Operation(
    val action: Action,
    val extra: String? = null,
    val player: Player? = null,
)

enum class Action {
    ADD,
    REMOVE,
    UPDATE,
    GET
}