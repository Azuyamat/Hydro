package com.azuyamat.blixify.pickaxe

import com.azuyamat.blixify.Logger.warning
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.plugin.java.JavaPlugin

class PickaxeManager(private val plugin: JavaPlugin) {

    val pickaxes = mutableMapOf<String, Pickaxe>()

    init {
        load()
    }

    fun getPickaxe(name: String): Pickaxe? {
        return pickaxes[name]
    }

    fun reload() {
        pickaxes.clear()
        load()
    }

    private fun load() {
        val configFile = plugin.dataFolder.resolve("pickaxes.yml")
        val config = YamlConfiguration.loadConfiguration(configFile)
            .getConfigurationSection("pickaxes")

        if (config == null) {
            warning("Pickaxes section does not exist in pickaxes.yml")
            return
        }

        for (key in config.getKeys(false)) {

            val section = config.getConfigurationSection(key)
            if (section == null) {
                warning("Pickaxe $key does not exist in pickaxes.yml")
                continue
            }

            // Name
            val name = section.getString("name") ?: key
            val parsedName = MiniMessage.miniMessage().deserialize(name)

            // Material
            val material = section.getString("material")?: "DIAMOND_PICKAXE"
                .uppercase().replace(" ", "_")
            val parsedMaterial = Material.getMaterial(material) ?: Material.DIAMOND_PICKAXE

            // Lore
            val lore = section.getStringList("lore")
            val parsedLore = lore.map {lore ->
                var lore = lore
                    .replace("{id}", key)

                MiniMessage.miniMessage().deserialize(lore)
            }

            // Enchantments
            val enchantments = section.getStringList("enchantments")
            val parsedEnchantments = enchantments.mapNotNull { Enchantment.getByName(it) }

            // Unbreakable
            val unbreakable = section.getBoolean("unbreakable", true)

            // Flags
            val flags = section.getStringList("flags")
            val parsedFlags = flags.mapNotNull { p -> ItemFlag.values().find { it.name.equals(p, true) } }

            // Create pickaxe
            pickaxes[key] = Pickaxe(
                id = key,
                name = parsedName,
                material = parsedMaterial,
                lore = parsedLore,
                enchantments = parsedEnchantments,
                unbreakable = unbreakable,
                flags = parsedFlags,
            )
        }
    }
}