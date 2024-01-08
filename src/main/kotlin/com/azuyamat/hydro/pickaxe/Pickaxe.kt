package com.azuyamat.hydro.pickaxe

import com.azuyamat.hydro.pickaxeManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

data class Pickaxe(
    val id: String,
    val name: Component = Component.text("Pickaxe", NamedTextColor.AQUA),
    val material: Material = Material.DIAMOND_PICKAXE,
    val lore: List<Component> = listOf(),
    val enchantments: List<Enchantment> = listOf(),
    val unbreakable: Boolean = true,
    val flags: List<ItemFlag> = listOf(),
) {

    fun create() = createPickaxe(id)
}

fun createPickaxe(type: String): ItemStack {

    val info = pickaxeManager.getPickaxe(type) ?: throw Exception("Pickaxe type $type does not exist")

    val pickaxe = ItemStack(info.material)
    val meta = pickaxe.itemMeta

    meta.displayName(info.name)
    meta.isUnbreakable = info.unbreakable
    info.flags.forEach { meta.addItemFlags(it) }
    meta.lore(info.lore)

    pickaxe.itemMeta = meta
    info.enchantments.forEach { pickaxe.addUnsafeEnchantment(it, 1) }

    return pickaxe
}