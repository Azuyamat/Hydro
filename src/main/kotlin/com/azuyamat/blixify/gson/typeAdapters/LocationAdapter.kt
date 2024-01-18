package com.azuyamat.blixify.gson.typeAdapters

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.Bukkit
import org.bukkit.Location

class LocationAdapter: TypeAdapter<Location>() {
    override fun write(out: JsonWriter?, value: Location?) {
        if (out == null || value == null) return
        out.beginObject()
        out.name("world").value(value.world?.name)
        out.name("x").value(value.x)
        out.name("y").value(value.y)
        out.name("z").value(value.z)
        out.name("yaw").value(value.yaw)
        out.name("pitch").value(value.pitch)
        out.endObject()
    }

    override fun read(`in`: JsonReader?): Location {
        if (`in` == null) return Location(null, 0.0, 0.0, 0.0)
        `in`.beginObject()
        var worldName: String? = ""
        var x = 0.0
        var y = 0.0
        var z = 0.0
        var yaw = 0f
        var pitch = 0f

        while (`in`.hasNext()) {
            when (`in`.nextName()) {
                "world" -> worldName = `in`.nextString()
                "x" -> x = `in`.nextDouble()
                "y" -> y = `in`.nextDouble()
                "z" -> z = `in`.nextDouble()
                "yaw" -> yaw = `in`.nextDouble().toFloat()
                "pitch" -> pitch = `in`.nextDouble().toFloat()
            }
        }
        `in`.endObject()

        return Location(Bukkit.getWorld(worldName!!), x, y, z, yaw, pitch)
    }


}