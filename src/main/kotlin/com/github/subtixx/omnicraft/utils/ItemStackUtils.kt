package com.github.subtixx.omnicraft.utils

import com.github.subtixx.omnicraft.mod.ModDataComponentTypes
import net.minecraft.ChatFormatting
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.*
import kotlin.math.min


fun ItemStack.isItemFuzzyEqual(stack2: ItemStack): Boolean {
    if (isSameOreDictStack(stack2)) {
        return true
    }
    if (item !== stack2.item) return false
    return this == stack2
}

fun ItemStack.isSameOreDictStack(stack2: ItemStack): Boolean {
    return tags.anyMatch { s1: TagKey<Item?> -> stack2.tags.anyMatch { s2: TagKey<Item?> -> s2 === s1 } }
}

fun ItemStack.getCurrentFace(): Direction {
    return getOrDefault(ModDataComponentTypes.CURRENT_FACE, Direction.DOWN)
}

fun ItemStack.cycleCurrentFace(serverPlayer: ServerPlayer) {
    val diff = if (serverPlayer.isShiftKeyDown) -1 else 1
    var currentFace = getCurrentFace()
    currentFace = Direction.entries[(currentFace.ordinal + diff + Direction.entries.size) %
            Direction.entries.size]

    set(ModDataComponentTypes.CURRENT_FACE, currentFace)

    serverPlayer.connection.send(
        ClientboundSetActionBarTextPacket(
            Component.translatable(
                "tooltip.energizedpower.wrench.select_face",
                Component.translatable("tooltip.energizedpower.direction." + currentFace.serializedName).withStyle
                    (ChatFormatting.WHITE, ChatFormatting.BOLD)
            ).withStyle(ChatFormatting.GRAY)
        )
    )
}

fun ItemStack.encodeStack(ops: HolderLookup.Provider?): Tag? {
    if (ops == null) {
        return null
    }

    return save(ops)
}

object ItemStackUtils {
    fun combineItemStacks(itemStacks: List<ItemStack>): List<ItemStack> {
        val combinedItemStacks: MutableList<ItemStack> = LinkedList()
        for (itemStack in itemStacks) {
            var inserted = false
            var amountLeft = itemStack.count
            for (combinedItemStack in combinedItemStacks) {
                if (ItemStack.isSameItem(itemStack, combinedItemStack) && ItemStack.isSameItemSameComponents(
                        itemStack,
                        combinedItemStack
                    ) && combinedItemStack.maxStackSize > combinedItemStack.count
                ) {
                    val amount = min(
                        amountLeft.toDouble(),
                        (combinedItemStack.maxStackSize - combinedItemStack.count).toDouble()
                    )
                        .toInt()
                    amountLeft -= amount

                    combinedItemStack.grow(amount)

                    if (amountLeft == 0) {
                        inserted = true
                        break
                    }
                }
            }

            if (!inserted) combinedItemStacks.add(itemStack.copyWithCount(amountLeft))
        }

        return combinedItemStacks
    }
}