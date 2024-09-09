package com.github.subtixx.omnicraft.utils.extensions

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

fun Player.getBlocksInRange(radius: Int, filter: (Block) -> Boolean): List<Block> {
    val blocksInRange = mutableListOf<Block>()
    val level: Level = level()
    val playerPos: BlockPos = blockPosition()

    for (x in -radius..radius) {
        for (y in -radius..radius) {
            for (z in -radius..radius) {
                val pos = playerPos.offset(x, y, z)
                val block = level.getBlockState(pos).block

                if (filter(block)) continue

                blocksInRange.add(block)
            }
        }
    }
    return blocksInRange
}