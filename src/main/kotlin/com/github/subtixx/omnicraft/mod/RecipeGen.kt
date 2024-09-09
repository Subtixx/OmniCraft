package com.github.subtixx.omnicraft.mod

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class RecipeGen(packOutput: PackOutput, completableFuture: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(packOutput, completableFuture) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        tinyCoal(recipeOutput)
    }

    private fun tinyCoal(recipeOutput: RecipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COAL)
            .pattern("XXX")
            .pattern("X X")
            .pattern("XXX")
            .define('X', ModItems.TINY_COAL_ITEM)
            .unlockedBy("has_item", has(Items.COAL))
            .save(recipeOutput)

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHARCOAL)
            .pattern("XXX")
            .pattern("X X")
            .pattern("XXX")
            .define('X', ModItems.TINY_CHARCOAL_ITEM)
            .unlockedBy("has_item", has(Items.CHARCOAL))
            .save(recipeOutput)

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemStack(ModItems.TINY_COAL_ITEM, 8))
            .requires(Items.COAL)
            .unlockedBy("has_item", has(Items.COAL))
            .save(recipeOutput)

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemStack(ModItems.TINY_CHARCOAL_ITEM, 8))
            .requires(Items.CHARCOAL)
            .unlockedBy("has_item", has(Items.CHARCOAL))
            .save(recipeOutput)
    }
}