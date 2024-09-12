package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.tags.BlockTagsProvider
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.SimpleTier
import net.neoforged.neoforge.common.Tags

object ModTiers {
    val COPPER_TIER = SimpleTier(
        BlockTagsProvider.INCORRECT_FOR_COPPER_TOOL,
        200,
        5f,
        1.5f,
        20,
        { Ingredient.of(Tags.Items.INGOTS_COPPER) }
    )

    val BRONZE_TIER = SimpleTier(
        BlockTagsProvider.INCORRECT_FOR_BRONZE_TOOL,
        250,
        5f,
        2f,
        20,
        { Ingredient.of(ModResources.RESOURCES["bronze"]?.ingot) }
    )

    val STEEL_TIER = SimpleTier(
        BlockTagsProvider.INCORRECT_FOR_STEEL_TOOL,
        250,
        5f,
        2f,
        20,
        { Ingredient.of(ModResources.RESOURCES["steel"]?.ingot) }
    )
}