package com.github.subtixx.omnicraft.item

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType

class TinyCoalItem(properties: Properties) : Item(properties) {
    override fun getBurnTime(itemStack: ItemStack, recipeType: RecipeType<*>?): Int {
        return 200
    }
}