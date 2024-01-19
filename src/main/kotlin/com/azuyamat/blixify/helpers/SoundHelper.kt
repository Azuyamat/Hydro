package com.azuyamat.blixify.helpers

import com.azuyamat.blixify.enums.CustomSound
import org.bukkit.Location
import org.bukkit.entity.Player

class SoundHelper(
    private var sound: CustomSound,
    private vararg var target: Player,
    private var volume: Float? = null,
    private var pitch: Float? = null,
) {

    fun setTarget(vararg target: Player) {
        this.target = target
    }

    fun setSound(sound: CustomSound) {
        this.sound = sound
    }

    fun setVolume(volume: Float) {
        this.volume = volume
    }

    fun setPitch(pitch: Float) {
        this.pitch = pitch
    }

    fun play() {
        target.forEach { it.playSound(it.location, sound.minecraftSound, volume ?: sound.volume, pitch ?: sound.pitch) }
    }

    fun playFromLocation(location: Location) {
        target.forEach { it.playSound(location, sound.minecraftSound, volume ?: sound.volume, pitch ?: sound.pitch) }
    }
}