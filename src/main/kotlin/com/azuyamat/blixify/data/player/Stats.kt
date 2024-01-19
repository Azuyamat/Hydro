package com.azuyamat.blixify.data.player

data class Stats(
    var kills: Int = 0,
    var deaths: Int = 0, // If death > 2,147,483,647, player = noob
    var blocksMinedNaturally: Long = 0L,
    var blocksMinedWithEnchants: Long = 0L,
)
