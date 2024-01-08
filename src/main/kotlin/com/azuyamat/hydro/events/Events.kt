package com.azuyamat.hydro.events

import com.azuyamat.hydro.Logger.info
import com.azuyamat.hydro.Logger.success
import com.azuyamat.hydro.eventsPackageName
import com.azuyamat.hydro.instance
import org.bukkit.event.Listener
import org.reflections.Reflections

object Events {

    fun registerEvents() {

        val start = System.currentTimeMillis()

        val eventsPackage = Reflections(eventsPackageName)
        val events = eventsPackage.getSubTypesOf(Listener::class.java)

        events.forEach(::registerEvent)

        val end = System.currentTimeMillis()
        val time = end - start

        info("Registered ${events.size} events in $time ms")
    }

    private fun registerEvent(event: Class<out Listener>) {

        instance.server.pluginManager.registerEvents(
            event.constructors.first().newInstance() as Listener, instance
        )

        success("Registered event: ${event.simpleName}")
    }
}