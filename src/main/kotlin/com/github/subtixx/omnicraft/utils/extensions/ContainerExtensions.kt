package com.github.subtixx.omnicraft.utils.extensions

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack

fun Container.canInsertItemIntoSlot(slot: Int, itemStack: ItemStack): Boolean {
    val inventoryItemStack: ItemStack = getItem(slot)

    return inventoryItemStack.isEmpty || (ItemStack.isSameItemSameComponents(inventoryItemStack, itemStack) &&
            inventoryItemStack.maxStackSize >= inventoryItemStack.count + itemStack.count)
}
