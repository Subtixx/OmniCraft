package com.github.subtixx.omnicraft.block

import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.context.UseOnContext

interface IUsableWithWrench {
    fun onUseWrench(
        useOnContext: UseOnContext,
        selectedFace: Direction,
        nextPreviousValue: Boolean
    ): InteractionResult
}