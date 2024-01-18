package com.azuyamat.blixify.pickaxe

import com.azuyamat.blixify.data.player.getPlayerData
import com.azuyamat.blixify.instance
import com.azuyamat.blixify.parse
import com.azuyamat.blixify.pickaxeManager
import com.azuyamat.blixify.titleCase
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

val pickaxeIdKey = NamespacedKey(instance, "ID")

data class Pickaxe(
    val id: String,
    val name: Component = Component.text("Pickaxe", NamedTextColor.AQUA),
    val material: Material = Material.DIAMOND_PICKAXE,
    val lore: List<Component> = listOf(),
    val enchantments: List<Enchantment> = listOf(),
    val unbreakable: Boolean = true,
    val flags: List<ItemFlag> = listOf(),
    val namespacedKey: NamespacedKey
) {

    fun create() = createPickaxe(id)
}

fun createPickaxe(type: String): ItemStack {

    val info = pickaxeManager.getPickaxe(type) ?: throw Exception("Pickaxe type $type does not exist")

    val pickaxe = ItemStack(info.material)
    val meta = pickaxe.itemMeta

    meta.persistentDataContainer.set(pickaxeIdKey, PersistentDataType.STRING, info.id)

    // Take note: Removing italics
    val name = info.name.decoration(TextDecoration.ITALIC, false)
    meta.displayName(name)
    meta.isUnbreakable = info.unbreakable
    info.flags.forEach { meta.addItemFlags(it) }
    meta.lore(info.lore)

    pickaxe.itemMeta = meta
    info.enchantments.forEach { pickaxe.addUnsafeEnchantment(it, 1) }

    return pickaxe
}

fun ItemStack.updateLore(player: Player) : ItemStack {

    val pickaxe = this

    val meta = itemMeta
    val id = itemMeta.persistentDataContainer.get(pickaxeIdKey, PersistentDataType.STRING) ?: return pickaxe
    val info = pickaxeManager.getPickaxe(id) ?: return pickaxe
    val playerData = player.getPlayerData()

    val lore = info.lore.toMutableList()
    val enchantLore = playerData.enchants.map {

        val name = it.key.name.titleCase()
        val level = it.value
        val parsedBar = Component.text().content("|").color(it.key.color).decoration(TextDecoration.BOLD, true).build()
        val parsedLevel = Component.text().content(level.toString()).color(it.key.color).decoration(TextDecoration.BOLD, false).build()
        val mainParsed = " <gray>$name<gray>: ".parse().decoration(TextDecoration.BOLD, false)
        parsedBar.append(mainParsed).append(parsedLevel)
    }
    lore.addAll(enchantLore)
    meta.lore(lore.map { it.decoration(TextDecoration.ITALIC, false) })
    itemMeta = meta
    return pickaxe
}