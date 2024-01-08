package com.azuyamat.blixify.commands.completions.list

import com.azuyamat.blixify.commands.completions.Completion
import com.azuyamat.blixify.pickaxeManager

class PickaxeCompletion : Completion {

    override fun complete() = pickaxeManager.pickaxes.keys.toList()
}