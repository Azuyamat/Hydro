package com.azuyamat.blixify.helpers

import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class NumberHelper(
    private val number: Number
) {

    // Separate thousands with commas
    fun toCommas() = String.format("%,d", number)

    // Convert number to 1K, 1M, 1B, 1T, etc.
    fun toShorten(): String {
        val suffix = charArrayOf(' ', 'K', 'M', 'B', 'T', 'P', 'E')
        val numValue: Long = number.toLong()
        val value = floor(log10(numValue.toDouble())).toInt()
        val base = value / 3
        if (value >= 3 && base < suffix.size) {
            val formattedNumber = numValue / 10.0.pow((base * 3).toDouble())
            return if (formattedNumber % 1 == 0.0) {
                String.format("%.0f %c", formattedNumber, suffix[base])
            } else {
                String.format("%.1f %c", formattedNumber, suffix[base])
            }
        } else {
            return String.format("%.0f", numValue.toDouble())
        }
    }

    // Convert number to a bar like this: <gray>[<main>▐▐▐▐<gray>▐▐▐]
    fun toBar(max: Number, bars: Int): String {
        val filledBars = (number.toDouble() / max.toDouble() * bars.toDouble()).toInt()
        val emptyBars = bars - filledBars
        return "<main>[<main>${"|".repeat(filledBars)}<gray>${"|".repeat(emptyBars)}<main>]"
    }
}