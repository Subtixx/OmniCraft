@file:Suppress("unused")

package com.github.subtixx.omnicraft.utils.extensions

import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

fun VoxelShape.testOcclusion(shape2: VoxelShape): Boolean {
    return toAabbs().stream()
        .anyMatch { s: AABB -> shape2.toAabbs().stream().anyMatch { other: AABB -> s.intersects(other) } }
}

fun VoxelShape.rotate(facing: Direction): VoxelShape {
    var out = Shapes.empty()
    for (aabb in toAabbs()) {
        val newAABB = aabb.rotate(facing)
        out = Shapes.or(
            out,
            Block.box(
                newAABB.minX * 16,
                newAABB.minY * 16,
                newAABB.minZ * 16,
                newAABB.maxX * 16,
                newAABB.maxY * 16,
                newAABB.maxZ * 16
            )
        )
    }
    return out
}