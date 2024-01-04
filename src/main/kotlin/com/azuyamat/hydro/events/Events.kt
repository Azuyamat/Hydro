package com.azuyamat.hydro.events

import com.azuyamat.hydro.Logger.info
import com.azuyamat.hydro.eventsPackageName
import com.azuyamat.hydro.instance
import org.bukkit.event.Listener
import org.reflections.Reflections

object Events {

    fun registerEvents() {

        val eventsPackage = Reflections(eventsPackageName)
        val events = eventsPackage.getSubTypesOf(Listener::class.java)

        events.forEach(::registerEvent)
    }

    private fun registerEvent(event: Class<out Listener>) {
        instance.server.pluginManager.registerEvents(
            event.constructors.first().newInstance() as Listener, instance
        )

        info("Registered event: ${event.simpleName}")
    }
}