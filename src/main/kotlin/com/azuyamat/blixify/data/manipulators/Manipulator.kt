package com.azuyamat.blixify.data.manipulators

import com.azuyamat.blixify.data.Data
import java.util.UUID

interface Manipulator<T : Data, E> {

    // Cache
    var cache: MutableMap<E, T>

    // Save data to a destination
    fun save(data: T, destination: Destination = Destination.LOCAL)

    // Load data from a destination
    fun load(id: E, destination: Destination = Destination.LOCAL): T?

    // Cache data
    fun cache(data: T)

    // Cache data from UUID
    fun cache(data: E)

    // Un-cache data
    fun unCache(uuid: E)
}

enum class Destination {
    LOCAL,
    MONGO,
    // REMOTE? Would be cool to have a remote database that isn't mongo
}