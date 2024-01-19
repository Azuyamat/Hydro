package com.azuyamat.blixify.commands.completions.impl

import com.azuyamat.blixify.commands.completions.Completion
import com.azuyamat.blixify.enums.CustomSound

class SoundCompletion : Completion {

    override fun complete(): List<String> {
        return CustomSound.entries.map { it.name.lowercase() }
    }
}