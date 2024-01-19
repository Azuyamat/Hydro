package com.azuyamat.blixify.enums

import org.bukkit.Sound

enum class CustomSound(
    val minecraftSound: Sound,
    val volume: Float = 0.5f,
    val pitch: Float = 1f,
) {
    SUCCESS(Sound.ENTITY_PLAYER_LEVELUP),
    ERROR(Sound.ENTITY_VILLAGER_NO),
    CLICK(Sound.UI_BUTTON_CLICK),
    POP(Sound.ENTITY_EXPERIENCE_ORB_PICKUP),
    POP2(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 2f),
    POP3(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 0.5f),
}