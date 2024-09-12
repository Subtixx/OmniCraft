package com.github.subtixx.omnicraft.gen

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.mod.ModItems
import com.github.subtixx.omnicraft.mod.ModResources
import com.github.subtixx.omnicraft.resources.WorldResource
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class RecipeGen(packOutput: PackOutput, completableFuture: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(packOutput, completableFuture) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        tinyCoal(recipeOutput)

        ModResources.RESOURCES.forEach { worldResource(it.value, recipeOutput) }
    }

    private fun worldResource(worldResource: WorldResource, recipeOutput: RecipeOutput) {
        if (worldResource.addRecipe) {
            if (worldResource.storageBlock != null && worldResource.storageBlockItem != null && worldResource.ingot != null) {
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, worldResource.storageBlock!!)
                    .pattern("XXX")
                    .pattern("XXX")
                    .pattern("XXX")
                    .define('X', worldResource.ingot!!)
                    .unlockedBy("has_item", has(worldResource.ingot!!))
                    .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Omnicraft.ID,
                        RecipeBuilder.getDefaultRecipeId(worldResource.storageBlockItem!!).path))

                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemStack(worldResource.ingot!!.get(), 9))
                    .requires(worldResource.storageBlock!!)
                    .unlockedBy("has_item", has(worldResource.storageBlock!!))
                    .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Omnicraft.ID,
                        RecipeBuilder.getDefaultRecipeId(worldResource.ingot!!).path))
            }

            if (worldResource.nugget != null && worldResource.ingot != null) {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemStack(worldResource.nugget!!.get(), 9))
                    .requires(worldResource.ingot!!)
                    .unlockedBy("has_item", has(worldResource.ingot!!))
                    .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(Omnicraft.ID,
                        RecipeBuilder.getDefaultRecipeId(worldResource.nugget!!).path))
            }
        }

        if (worldResource.addSmelting && worldResource.rawOreItem != null && worldResource.ingot != null) {
            oreSmelting(
                recipeOutput,
                listOf(worldResource.rawOreItem),
                RecipeCategory.MISC,
                worldResource.ingot!!,
                worldResource.smeltExperience,
                worldResource.smeltTime,
                "smelting_${worldResource.name}_ingot"
            )
        }
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