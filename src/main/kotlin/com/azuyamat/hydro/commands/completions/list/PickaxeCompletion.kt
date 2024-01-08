package com.azuyamat.hydro.commands.completions.list

import com.azuyamat.hydro.commands.completions.Completion
import com.azuyamat.hydro.pickaxeManager

class PickaxeCompletion : Completion {

    override fun complete() = pickaxeManager.pickaxes.keys.toList()
}