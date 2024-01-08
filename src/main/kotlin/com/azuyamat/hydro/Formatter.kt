package com.azuyamat.hydro

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object Formatter {

    private val mm = MiniMessage.miniMessage()

    fun format(string: String): Component {
        return mm.deserialize(string)
    }
}