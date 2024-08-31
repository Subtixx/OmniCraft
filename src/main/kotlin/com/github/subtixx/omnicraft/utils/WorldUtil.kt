package com.github.subtixx.omnicraft.utils

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks

abstract class WorldUtil {
    companion object {
        fun getBlocksInRange(player: Player, radius: Int, filter: (Block) -> Boolean): List<Block> {
            val blocksInRange = mutableListOf<Block>()
            val level: Level = player.level()
            val playerPos: BlockPos = player.blockPosition()

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
    }
}