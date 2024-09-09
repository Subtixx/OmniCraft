package com.github.subtixx.omnicraft.utils

import com.github.subtixx.omnicraft.block.IRotatable
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.ChestBlockEntity
import net.minecraft.world.level.block.piston.PistonBaseBlock
import net.minecraft.world.level.block.piston.PistonHeadBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.*
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import java.util.*


object BlockUtils {
    fun tryRotatingBlockAndConnected(
        face: Direction,
        ccw: Boolean,
        targetPos: BlockPos,
        level: Level,
        hit: Vec3
    ): Direction? {
        val state: BlockState = level.getBlockState(targetPos)
        if (state.block is IRotatable) {
            val rotatable: IRotatable = state.block as IRotatable
            return rotatable.rotateOverAxis(
                state,
                level,
                targetPos,
                if (ccw) Rotation.COUNTERCLOCKWISE_90 else Rotation.CLOCKWISE_90,
                face,
                hit
            )
        }
        val special: Direction? = tryRotatingSpecial(face, ccw, targetPos, level, state, hit)
        if (special != null) return special

        var ret: Direction? = tryRotatingBlock(face, ccw, targetPos, level, state, hit)
        if (ret == null) {
            ret = tryRotatingBlock(Direction.UP, ccw, targetPos, level, level.getBlockState(targetPos), hit)
        }
        return ret
    }

    fun tryRotatingBlock(
        face: Direction,
        ccw: Boolean,
        targetPos: BlockPos,
        level: Level,
        hit: Vec3
    ): Direction? {
        return tryRotatingBlock(face, ccw, targetPos, level, level.getBlockState(targetPos), hit)
    }

    fun tryRotatingBlock(
        dir: Direction,
        ccw: Boolean,
        targetPos: BlockPos,
        level: Level,
        state: BlockState,
        hit: Vec3
    ): Direction? {
        if (state.block is IRotatable) {
            val rotatable: IRotatable = state.block as IRotatable
            return rotatable.rotateOverAxis(
                state,
                level,
                targetPos,
                if (ccw) Rotation.COUNTERCLOCKWISE_90 else Rotation.CLOCKWISE_90,
                dir,
                hit
            )
        }

        var rotated: BlockState = getRotatedState(dir, ccw, targetPos, level, state) ?: return null
        if (!rotated.canSurvive(level, targetPos)) return null
        rotated = Block.updateFromNeighbourShapes(rotated, level, targetPos)
        if (rotated === state) return null
        if (level !is ServerLevel) return dir
        level.setBlock(targetPos, rotated, 11)
        level.neighborChanged(rotated, targetPos, rotated.block, targetPos, false)
        return dir
    }

    fun getRotatedState(
        dir: Direction,
        ccw: Boolean,
        targetPos: BlockPos,
        world: Level,
        state: BlockState
    ): BlockState? {
        if (isBlacklisted(state)) return null

        val rot: Rotation = if (ccw) Rotation.COUNTERCLOCKWISE_90 else Rotation.CLOCKWISE_90
        val block: Block = state.block
        //horizontal facing blocks -easy
        if (dir.axis === Direction.Axis.Y) {
            var rotated: BlockState = state.rotate(world, targetPos, rot)
            //also hardcoding vanilla rotation methods cause some mods just dont implement rotate methods for their blocks
            //this could cause problems for mods that do and dont want it to be rotated but those should really be added to the blacklist
            if (rotated === state) {
                rotated = rotateVerticalStandard(state, rotated, rot)
            }
            return rotated
        } else if (state.hasProperty(BlockStateProperties.ATTACH_FACE) && state.hasProperty(
                BlockStateProperties.HORIZONTAL_FACING
            )
        ) {
            val res = getRotatedHorizontalFaceBlock(state, dir, ccw)
            if (res != null) return res
        }
        // 6 dir blocks blocks
        if (state.hasProperty(BlockStateProperties.FACING)) {
            return getRotatedDirectionalBlock(state, dir, ccw)
        }
        // axis blocks
        if (state.hasProperty(BlockStateProperties.AXIS)) {
            val targetAxis: Direction.Axis = state.getValue(BlockStateProperties.AXIS)
            val myAxis: Direction.Axis = dir.axis
            if (myAxis === Direction.Axis.X) {
                return state.setValue(
                    BlockStateProperties.AXIS,
                    if (targetAxis === Direction.Axis.Y) Direction.Axis.Z else Direction.Axis.Y
                )
            } else if (myAxis === Direction.Axis.Z) {
                return state.setValue(
                    BlockStateProperties.AXIS,
                    if (targetAxis === Direction.Axis.Y) Direction.Axis.X else Direction.Axis.Y
                )
            }
        }
        if (block is StairBlock) {
            return getRotatedStairs(state, dir, ccw)
        }
        if (state.hasProperty(SlabBlock.TYPE)) {
            val type = state.getValue(SlabBlock.TYPE)
            if (type == SlabType.DOUBLE) return null
            return state.setValue(
                SlabBlock.TYPE,
                if (type == SlabType.BOTTOM) SlabType.TOP else SlabType.BOTTOM
            )
        }
        if (state.hasProperty(TrapDoorBlock.HALF)) {
            return state.cycle(TrapDoorBlock.HALF)
        }
        return null
    }

    private fun rotateVerticalStandard(state: BlockState, rotated: BlockState, rot: Rotation): BlockState {
        return when {
            state.hasProperty(BlockStateProperties.FACING) -> {
                state.setValue(
                    BlockStateProperties.FACING,
                    rot.rotate(state.getValue(BlockStateProperties.FACING))
                )
            }

            state.hasProperty(BlockStateProperties.HORIZONTAL_FACING) -> {
                state.setValue(
                    BlockStateProperties.HORIZONTAL_FACING,
                    rot.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING))
                )
            }

            state.hasProperty(RotatedPillarBlock.AXIS) -> {
                RotatedPillarBlock.rotatePillar(state, rot)
            }

            state.hasProperty(BlockStateProperties.HORIZONTAL_AXIS) -> {
                state.cycle(BlockStateProperties.HORIZONTAL_AXIS)
            }

            else -> rotated
        }
    }

    private fun tryRotatingSpecial(
        face: Direction,
        ccw: Boolean,
        pos: BlockPos,
        level: Level,
        state: BlockState,
        hit: Vec3
    ): Direction? {
        val b = state.block
        val rot = if (ccw) Rotation.COUNTERCLOCKWISE_90 else Rotation.CLOCKWISE_90
        if (state.hasProperty(BlockStateProperties.ROTATION_16)) {
            var r = state.getValue(BlockStateProperties.ROTATION_16)
            r += (if (ccw) -1 else 1)
            if (r < 0) r += 16
            r %= 16
            level.setBlock(pos, state.setValue(BlockStateProperties.ROTATION_16, r), 2)
            return Direction.UP
        }

        if (state.hasProperty(BlockStateProperties.SHORT) || state.hasProperty(PistonBaseBlock.EXTENDED) && state.hasProperty(
                PistonBaseBlock.FACING
            )
        ) {
            val opt = rotatePistonHead(state, pos, level, face, ccw)
            if (opt != null) return opt
        }
        if (b is BedBlock) {
            return rotateBedBlock(face, pos, level, state, rot)
        }
        if (b is ChestBlock) {
            return rotateDoubleChest(face, pos, level, state, rot)
        }
        return null
    }

    fun getRotatedHorizontalFaceBlock(original: BlockState, axis: Direction, ccw: Boolean): BlockState? {
        val facingDir = original.getValue(BlockStateProperties.HORIZONTAL_FACING)
        if (facingDir.axis === axis.axis) return null

        val face = original.getValue(BlockStateProperties.ATTACH_FACE) ?: return null
        return when (face) {
            AttachFace.FLOOR -> original.setValue(BlockStateProperties.ATTACH_FACE, AttachFace.WALL)
                .setValue(
                    BlockStateProperties.HORIZONTAL_FACING,
                    if (ccw) axis.clockWise else axis.counterClockWise
                )

            AttachFace.CEILING -> original.setValue(BlockStateProperties.ATTACH_FACE, AttachFace.WALL)
                .setValue(
                    BlockStateProperties.HORIZONTAL_FACING,
                    if (!ccw) axis.clockWise else axis.counterClockWise
                )

            AttachFace.WALL -> {
                val newCcw = ccw xor (axis.axisDirection != Direction.AxisDirection.POSITIVE)
                original.setValue(
                    BlockStateProperties.ATTACH_FACE,
                    if ((facingDir.axisDirection == Direction.AxisDirection.POSITIVE) xor newCcw) AttachFace.CEILING else AttachFace.FLOOR
                )
            }
        }
    }


    private fun rotateDoubleChest(
        face: Direction,
        pos: BlockPos,
        level: Level,
        state: BlockState,
        rot: Rotation
    ): Direction? {
        if (state.getValue(ChestBlock.TYPE) == ChestType.SINGLE) return null
        val newChest: BlockState = state.rotate(level, pos, rot)
        val oldPos = pos.relative(ChestBlock.getConnectedDirection(state))
        val targetPos = pos.relative(ChestBlock.getConnectedDirection(newChest))
        if (!level.getBlockState(targetPos).canBeReplaced()) {
            return null
        }

        val connectedNewState: BlockState =
            level.getBlockState(oldPos).rotate(level, oldPos, rot)
        level.setBlock(targetPos, connectedNewState, 2)
        level.setBlock(pos, newChest, 2)

        val tile = level.getBlockEntity(oldPos)
        if (tile != null) {
            val tag = tile.saveWithoutMetadata(level.registryAccess())
            if (level.getBlockEntity(targetPos) is ChestBlockEntity) {
                val newChestTile = level.getBlockEntity(targetPos) as ChestBlockEntity
                newChestTile.loadWithComponents(tag, level.registryAccess())
            }
            tile.setRemoved()
        }

        level.setBlockAndUpdate(oldPos, Blocks.AIR.defaultBlockState())
        return face
    }


    fun rotatePistonHead(
        state: BlockState,
        pos: BlockPos,
        level: Level,
        face: Direction,
        ccw: Boolean
    ): Direction? {
        val newBase: BlockState = getRotatedDirectionalBlock(state, face, ccw) ?: return null
        val oldHeadPos: BlockPos
        val oldHead: BlockState
        val newHeadPos: BlockPos
        if (state.hasProperty(PistonHeadBlock.SHORT)) {
            oldHeadPos = pos.relative(state.getValue(PistonHeadBlock.FACING).opposite)
            oldHead = level.getBlockState(oldHeadPos)
            if (!oldHead.hasProperty(PistonBaseBlock.EXTENDED)) return null
            newHeadPos = pos.relative(newBase.getValue(PistonHeadBlock.FACING).opposite)
        } else if (state.hasProperty(PistonBaseBlock.EXTENDED)) {
            oldHeadPos = pos.relative(state.getValue(PistonHeadBlock.FACING))
            oldHead = level.getBlockState(oldHeadPos)
            if (!oldHead.hasProperty(PistonHeadBlock.SHORT)) return null
            newHeadPos = pos.relative(newBase.getValue(PistonBaseBlock.FACING))
        } else return null

        if (level.getBlockState(newHeadPos).canBeReplaced()) {
            val rotatedHead: BlockState? = getRotatedDirectionalBlock(oldHead, face, ccw)
            if (rotatedHead != null) {
                level.setBlock(newHeadPos, rotatedHead, 2)
                level.setBlock(pos, newBase, 2)
                level.removeBlock(oldHeadPos, false)
                return face
            }
        }

        return null
    }

    fun rotateBedBlock(
        face: Direction,
        pos: BlockPos,
        level: Level,
        state: BlockState,
        rot: Rotation
    ): Direction? {
        val newBed: BlockState = state.rotate(level, pos, rot)
        val oldPos = pos.relative(getConnectedBedDirection(state))
        val targetPos = pos.relative(getConnectedBedDirection(newBed))
        if (level.getBlockState(targetPos).canBeReplaced()) {
            level.setBlock(targetPos, level.getBlockState(oldPos).rotate(level, oldPos, rot), 2)
            level.setBlock(pos, newBed, 2)
            level.removeBlock(oldPos, false)
            return face
        }
        return null
    }

    fun getConnectedBedDirection(bedState: BlockState): Direction {
        val part = bedState.getValue(BedBlock.PART)
        val dir = bedState.getValue(BedBlock.FACING)
        return if (part == BedPart.FOOT) dir else dir.opposite
    }

    fun getRotatedStairs(state: BlockState, axis: Direction, ccw: Boolean): BlockState? {
        var facing = state.getValue(StairBlock.FACING)
        if (facing.axis === axis.axis) return null

        val flipped = (axis.axisDirection == Direction.AxisDirection.POSITIVE) xor ccw
        var half = state.getValue(StairBlock.HALF)
        val top = half == Half.TOP
        val positive = facing.axisDirection == Direction.AxisDirection.POSITIVE

        if ((top xor positive) xor flipped) {
            half = if (top) Half.BOTTOM else Half.TOP
        } else {
            facing = facing.opposite
        }

        return state.setValue(StairBlock.HALF, half).setValue(StairBlock.FACING, facing)
    }


    fun getRotatedDirectionalBlock(state: BlockState, axis: Direction, ccw: Boolean): BlockState? {
        var targetNormal: Vec3 = state.getValue(BlockStateProperties.FACING).normal.toVec3()
        val myNormal: Vec3 = axis.normal.toVec3()
        if (!ccw) targetNormal = targetNormal.scale(-1.0)

        val rotated = myNormal.cross(targetNormal)
        // not on same axis, can rotate
        if (rotated != Vec3.ZERO) {
            val newDir = Direction.getNearest(rotated.x(), rotated.y(), rotated.z())
            return state.setValue(BlockStateProperties.FACING, newDir)
        }
        return null
    }

    private fun isBlacklisted(state: BlockState): Boolean {
        // double blocks
        if (state.block is BedBlock) return true
        if (state.hasProperty(BlockStateProperties.CHEST_TYPE)) {
            if (state.getValue(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE) return true
        }
        // no piston bases
        if (state.hasProperty(BlockStateProperties.EXTENDED)) {
            if (state.getValue(BlockStateProperties.EXTENDED)) return true
        }
        // nor piston arms
        if (state.hasProperty(BlockStateProperties.SHORT)) return true

        //return state.`is`(ModTags.ROTATION_BLACKLIST)

        return false
    }
}