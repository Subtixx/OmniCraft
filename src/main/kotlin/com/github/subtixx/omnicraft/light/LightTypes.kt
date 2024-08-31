package com.github.subtixx.omnicraft.light

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.function.Function


enum class LightTypes
    (
    val voxelShape: VoxelShape,
    val flameOffset: Vec3,
    val keyFactory: Function<BlockPos, String>,
    val lightFactory: Function<BlockPos, IEntityBlockingLight>
) {
    MegaTorch(
        Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0),
        Vec3(.5, 1.0, .5),
        Function<BlockPos, String> { pos: BlockPos -> "MT_" + pos.x + "_" + pos.y + "_" + pos.z },
        Function<BlockPos, IEntityBlockingLight> { pos: BlockPos ->
            MegaTorchEntityBlockingLight(
                pos
            )
        }
    ),
}
