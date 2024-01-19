package com.azuyamat.blixify.enums

import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

enum class Enchant(
    val maxLevel: Long,
    val minChance: Double = 0.0,
    val maxChance: Double = 1.0,
    val chanceIncrement: Double = 0.0,
    val isPassive: Boolean = false,
    val isToggleable: Boolean = false,
    val color: TextColor = NamedTextColor.BLUE
) {
    EFFICIENCY(255, isPassive = true, color = NamedTextColor.BLUE),
    FORTUNE(3, 0.05, 0.1, 0.01, color = NamedTextColor.GOLD),
    CHARITY(3, 0.05, 0.1, 0.01, color = NamedTextColor.RED),
    DRILL(3, 0.05, 0.1, 0.01, color = NamedTextColor.DARK_RED),
    GLAZE(3, 0.05, 0.1, 0.01, color = NamedTextColor.DARK_PURPLE),
    GREED(3, 0.05, 0.1, 0.01, color = NamedTextColor.DARK_GREEN),
    HASTE(3, 0.05, 0.1, 0.01, color = NamedTextColor.DARK_AQUA),
    JACKHAMMER(3, 0.05, 0.1, 0.01, color = NamedTextColor.DARK_BLUE),
    NUKE(30000, 0.05, 1.0, 0.01, color = NamedTextColor.LIGHT_PURPLE),
    SPEED(3, 0.05, 0.1, 0.01, color = NamedTextColor.AQUA),
    TOKEN_FINDER(3, 0.05, 0.1, 0.01, color = NamedTextColor.GREEN),
    JACKPOT(30000, 0.05, 1.0, 0.01, color = NamedTextColor.YELLOW),
}