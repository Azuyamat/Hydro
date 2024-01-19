package com.azuyamat.blixify

import com.azuyamat.blixify.helpers.Formatter.format

fun main() {

    val message = "<chatcolor:'304cd41a-7560-4097-8d3e-5f4b4ee71b3e'>Hey, I'm a message!"
    val component = format(message)

    println(component)
}