package com.github.subtixx.omnicraft.fluids

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.Item
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class SteamFluid : Fluid() {
    override fun getExplosionResistance(): Float {
        return 0.0f
    }

    override fun getBucket(): Item {
        return Blocks.AIR.asItem()
    }

    override fun canBeReplacedWith(
        state: FluidState,
        level: BlockGetter,
        pos: BlockPos,
        fluid: Fluid,
        direction: Direction
    ): Boolean {
        return false
    }

    override fun getFlow(blockReader: BlockGetter, pos: BlockPos, fluidState: FluidState): Vec3 {
        return Vec3.ZERO
    }

    override fun getTickDelay(level: LevelReader): Int {
        return 0
    }

    override fun getHeight(state: FluidState, level: BlockGetter, pos: BlockPos): Float {
        return 0.0f
    }

    override fun getOwnHeight(state: FluidState): Float {
        return 0.0f
    }

    override fun createLegacyBlock(state: FluidState): BlockState {
        return Blocks.AIR.defaultBlockState()
    }

    override fun isSource(state: FluidState): Boolean {
        return false
    }

    override fun getAmount(state: FluidState): Int {
        return 0
    }

    override fun getShape(state: FluidState, level: BlockGetter, pos: BlockPos): VoxelShape {
        return Shapes.empty()
    }
}