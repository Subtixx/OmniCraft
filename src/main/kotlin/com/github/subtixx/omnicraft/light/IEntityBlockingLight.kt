package com.github.subtixx.omnicraft.light

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

interface IEntityBlockingLight {
    fun shouldBlockEntity(entityType: EntityType<*>, spawnPos: Vec3, level: Level, spawnType: MobSpawnType): Boolean
    fun shouldBlockVillagePillagerSiege(pos: Vec3): Boolean
    fun shouldBlockVillageZombieRaid(pos: Vec3): Boolean

    val lightSerializerType: String
    val displayName: String
    val pos: BlockPos

    val boundingBox: AABB

    /**
     * Called frequently for cleanup purposes
     * Use this to check if the light should be removed from the registry, i.e. because a block no longer exists, etc
     * @param level the world
     * @return true if this instance should be removed from the registry, otherwise false.
     */
    fun cleanupCheck(level: Level): Boolean
}