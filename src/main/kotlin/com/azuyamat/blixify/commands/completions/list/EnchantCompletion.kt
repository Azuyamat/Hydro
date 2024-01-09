package com.azuyamat.blixify.commands.completions.list

import com.azuyamat.blixify.commands.completions.Completion
import com.azuyamat.blixify.enums.Enchant

class EnchantCompletion : Completion {

    override fun complete(): List<String> {
        return Enchant.entries.map { it.name.lowercase() }
    }
}