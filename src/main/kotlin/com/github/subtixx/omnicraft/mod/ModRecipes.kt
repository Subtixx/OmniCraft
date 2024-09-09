package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredRegister

object ModRecipes {
    val SERIALIZERS: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Omnicraft.ID)
    val TYPES: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, Omnicraft.ID)
}