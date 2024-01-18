package com.azuyamat.blixify.enums

import net.kyori.adventure.text.format.NamedTextColor

enum class CellRank(
    val defaultColor: NamedTextColor,
) {
    NONE(NamedTextColor.WHITE), // This should only be used a non-null value
    VISITOR(NamedTextColor.GRAY),
    MEMBER(NamedTextColor.GOLD),
    ADMIN(NamedTextColor.DARK_RED),
    OWNER(NamedTextColor.RED),
}