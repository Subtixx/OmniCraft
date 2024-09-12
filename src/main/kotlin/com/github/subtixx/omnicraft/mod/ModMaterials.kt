package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModMaterials {
    val REGISTRY: DeferredRegister<ArmorMaterial> = DeferredRegister.create(Registries.ARMOR_MATERIAL, Omnicraft.ID)
    val COPPER_ARMOR_MATERIAL by REGISTRY.register("copper") { ->
        ArmorMaterial(
            mapOf(
                Pair(ArmorItem.Type.BOOTS, 2),
                Pair(ArmorItem.Type.LEGGINGS, 4),
                Pair(ArmorItem.Type.CHESTPLATE, 6),
                Pair(ArmorItem.Type.HELMET, 2),
                Pair(ArmorItem.Type.BODY, 4),
            ),
            20,
            SoundEvents.ARMOR_EQUIP_IRON,
            { Ingredient.of(Items.COPPER_INGOT) },
            listOf(
                ArmorMaterial.Layer(
                    ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "copper"),
                ),
                ArmorMaterial.Layer(
                    ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "copper"), "_overlay", false
                ),
            ),
            0.0F,
            0.0F
        )
    }
}