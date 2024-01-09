package com.azuyamat.blixify.enums

enum class Enchant(
    val maxLevel: Long,
    val minChance: Double = 0.0,
    val maxChance: Double = 1.0,
    val chanceIncrement: Double = 0.0,
    val isPassive: Boolean = false,
) {
    EFFICIENCY(255, isPassive = true),
    FORTUNE(3, 0.05, 0.1, 0.01),
    CHARITY(3, 0.05, 0.1, 0.01),
    DRILL(3, 0.05, 0.1, 0.01),
    GLAZE(3, 0.05, 0.1, 0.01),
    GREED(3, 0.05, 0.1, 0.01),
    HASTE(3, 0.05, 0.1, 0.01),
    JACKHAMMER(3, 0.05, 0.1, 0.01),
    NUKE(3, 0.05, 0.1, 0.01),
    SPEED(3, 0.05, 0.1, 0.01),
    TOKEN_FINDER(3, 0.05, 0.1, 0.01),
}