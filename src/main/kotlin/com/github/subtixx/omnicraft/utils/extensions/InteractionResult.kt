@file:Suppress("unused")

package com.github.subtixx.omnicraft.utils.extensions

import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult

fun InteractionResult.toItemInteractionResult(): ItemInteractionResult {
    return when (this) {
        InteractionResult.SUCCESS, InteractionResult.SUCCESS_NO_ITEM_USED -> ItemInteractionResult.SUCCESS
        InteractionResult.CONSUME -> ItemInteractionResult.CONSUME
        InteractionResult.CONSUME_PARTIAL -> ItemInteractionResult.CONSUME_PARTIAL
        InteractionResult.PASS -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        InteractionResult.FAIL -> ItemInteractionResult.FAIL
    }
}