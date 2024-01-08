package com.azuyamat.blixify.events

import com.azuyamat.blixify.Logger.info
import com.azuyamat.blixify.Logger.success
import com.azuyamat.blixify.eventsPackageName
import com.azuyamat.blixify.instance
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