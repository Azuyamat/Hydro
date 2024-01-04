package com.azuyamat.hydro.data.player

data class PlayerStats(
    var kills: Int = 0,
    var deaths: Int = 0, // If death > 2,147,483,647, player = noob
    var blocksMined: Long = 0L,
)
