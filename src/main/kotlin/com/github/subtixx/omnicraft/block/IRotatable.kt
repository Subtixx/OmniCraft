package com.github.subtixx.omnicraft.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3

interface IRotatable {
    fun getRotatedState(
        state: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        rotation: Rotation,
        axis: Direction,
        hit: Vec3
    ): BlockState

    fun rotateOverAxis(
        state: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        rotation: Rotation,
        axis: Direction,
        hit: Vec3
    ): Direction? {
        var rotated = this.getRotatedState(state, world, pos, rotation, axis, hit)
        if (!rotated.canSurvive(world, pos)) {
            return null
        }

        rotated = Block.updateFromNeighbourShapes(rotated, world, pos)

        if (world is ServerLevel) {
            world.setBlock(pos, rotated, 11)
            //world.updateNeighborsAtExceptFromFacing(pos, rotated.block, axis.opposite);
        }
        this.onRotated(rotated, state, world, pos, rotation, axis, hit)
        return axis
    }

    fun onRotated(
        newState: BlockState,
        oldState: BlockState,
        world: LevelAccessor,
        pos: BlockPos,
        rotation: Rotation,
        axis: Direction,
        hit: Vec3
    )
}