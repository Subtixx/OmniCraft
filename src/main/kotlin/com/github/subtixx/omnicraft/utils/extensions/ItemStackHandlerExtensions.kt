package com.github.subtixx.omnicraft.utils.extensions

import net.minecraft.util.Mth
import net.neoforged.neoforge.items.ItemStackHandler
import kotlin.math.min

fun ItemStackHandler.getRedstoneSignal(): Int {
    var fullnessPercentSum = 0f
    var isEmptyFlag = true

    val size = slots
    for (i in 0 until size) {
        val item = getStackInSlot(i)
        if (!item.isEmpty) {
            fullnessPercentSum += item.count.toFloat() / min(
                item.maxStackSize.toFloat(),
                getSlotLimit(i).toFloat()
            )
            isEmptyFlag = false
        }
    }

    return min((Mth.floor(fullnessPercentSum / size * 14f) + (if (isEmptyFlag) 0 else 1)).toDouble(), 15.0).toInt()
}