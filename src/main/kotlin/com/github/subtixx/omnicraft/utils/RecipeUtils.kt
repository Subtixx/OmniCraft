package com.github.subtixx.omnicraft.utils

import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

object RecipeUtils {
    fun <C : RecipeInput?, T : Recipe<C>> isIngredientOfAny(
        level: Level,
        recipeType: RecipeType<T>,
        itemStack: ItemStack
    ): Boolean {
        val recipes: List<RecipeHolder<T>> = level.recipeManager.getAllRecipesFor(recipeType)

        return recipes
            .stream()
            .map { obj: RecipeHolder<T> -> obj.value() }
            .map { obj: T -> obj.ingredients }
            .anyMatch()
            { ingredients: NonNullList<Ingredient> ->
                ingredients.stream().anyMatch { ingredient: Ingredient -> ingredient.test(itemStack) }
            }
    }

    fun <C : RecipeInput, T : Recipe<C>> isResultOfAny(
        level: Level,
        recipeType: RecipeType<T>,
        itemStack: ItemStack
    ): Boolean {
        val recipes: List<RecipeHolder<T>> = level.recipeManager.getAllRecipesFor(recipeType)

        return recipes
            .stream()
            .map { obj: RecipeHolder<T> -> obj.value() }
            .map { recipe: T -> recipe.getResultItem(level.registryAccess()) }
            .anyMatch { stack: ItemStack -> ItemStack.isSameItemSameComponents(stack, itemStack) }
    }

    fun <C : RecipeInput, T : Recipe<C>> isRemainderOfAny(
        level: Level,
        recipeType: RecipeType<T>,
        container: C,
        itemStack: ItemStack
    ): Boolean {
        val recipes: List<RecipeHolder<T>> = level.recipeManager.getAllRecipesFor(recipeType)

        return recipes
            .stream()
            .map { obj: RecipeHolder<T> -> obj.value() }
            .map { recipe: T -> recipe.getRemainingItems(container) }
            .anyMatch() { remainingItems: NonNullList<ItemStack> ->
                remainingItems
                    .stream()
                    .anyMatch { item: ItemStack -> ItemStack.isSameItemSameComponents(item, itemStack) }
            }
    }
}