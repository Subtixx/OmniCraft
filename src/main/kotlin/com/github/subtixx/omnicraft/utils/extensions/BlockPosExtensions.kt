@file:Suppress("unused")

package com.github.subtixx.omnicraft.utils.extensions

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.AABB

fun BlockPos.boundingBox(range: Int): AABB {
    return AABB(
        (x - range).toDouble(),
        (y - range).toDouble(),
        (z - range).toDouble(),
        (x + range + 1).toDouble(),
        (y + range + 1).toDouble(),
        (z + range + 1).toDouble()
    )
}
