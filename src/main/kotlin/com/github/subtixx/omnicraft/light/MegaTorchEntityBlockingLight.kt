package com.github.subtixx.omnicraft.light

import com.github.subtixx.omnicraft.OmnicraftConfig
import com.github.subtixx.omnicraft.block.ModBlocks
import com.github.subtixx.omnicraft.utils.Vec3Extension
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape

class MegaTorchEntityBlockingLight(
    override val pos: BlockPos,
    override val lightSerializerType: String = "megatorch",
    override val displayName: String = "Megatorch",
) : IEntityBlockingLight {
    override val boundingBox: AABB
        get() = Vec3Extension.BoundingBox(
            pos,
            OmnicraftConfig.SERVER.megaTorchRadius.asInt
        )

    override fun shouldBlockEntity(
        entityType: EntityType<*>,
        spawnPos: Vec3,
        level: Level,
        spawnType: MobSpawnType
    ): Boolean {
        return Vec3Extension.DistCubic(
            spawnPos.x,
            spawnPos.y,
            spawnPos.z,
            this.pos,
            OmnicraftConfig.SERVER.megaTorchRadius.asInt
        )
    }

    override fun shouldBlockVillagePillagerSiege(pos: Vec3): Boolean {
        return Vec3Extension.DistCubic(
            pos.x(),
            pos.y(),
            pos.z(),
            this.pos,
            OmnicraftConfig.SERVER.megaTorchRadius.asInt
        )
    }

    override fun shouldBlockVillageZombieRaid(pos: Vec3): Boolean {
        return Vec3Extension.DistCubic(
            pos.x(),
            pos.y(),
            pos.z(),
            this.pos,
            OmnicraftConfig.SERVER.megaTorchRadius.asInt
        )
    }

    /**
     * Called frequently for cleanup purposes
     * Use this to check if the light should be removed from the registry, i.e. because a block no longer exists, etc
     *
     * @param level the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    override fun cleanupCheck(level: Level): Boolean {
        return level.isLoaded(this.pos) && level.getBlockState(pos).block !== ModBlocks.MEGA_TORCH
    }

    companion object {
        val SHAPE: VoxelShape = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0)
    }
}
