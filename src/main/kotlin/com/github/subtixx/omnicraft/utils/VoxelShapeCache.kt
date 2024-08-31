package com.github.subtixx.omnicraft.utils

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.common.collect.ImmutableSet
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import java.util.concurrent.TimeUnit

/**
 * Created by covers1624 on 3/10/20.
 */
object VoxelShapeCache {
    private val bbToShapeCache: Cache<AABB, VoxelShape?> = CacheBuilder.newBuilder()
        .expireAfterAccess(2, TimeUnit.HOURS)
        .build()
    private val cuboidToShapeCache: Cache<Cuboid6, VoxelShape?> = CacheBuilder.newBuilder()
        .expireAfterAccess(2, TimeUnit.HOURS)
        .build()

    private val mergeShapeCache: Cache<ImmutableSet<VoxelShape>, VoxelShape?> = CacheBuilder.newBuilder()
        .expireAfterAccess(2, TimeUnit.HOURS)
        .build()

    fun getShape(aabb: AABB): VoxelShape {
        var shape = bbToShapeCache.getIfPresent(aabb)
        if (shape == null) {
            shape = Shapes.create(aabb)
            bbToShapeCache.put(aabb, shape!!)
        }
        return shape
    }

    fun getShape(cuboid: Cuboid6): VoxelShape {
        var shape = cuboidToShapeCache.getIfPresent(cuboid)
        if (shape == null) {
            shape = Shapes.box(cuboid.min.x, cuboid.min.y, cuboid.min.z, cuboid.max.x, cuboid.max.y, cuboid.max.z)
            cuboidToShapeCache.put(cuboid, shape!!)
        }
        return shape
    }

    fun merge(shapes: ImmutableSet<VoxelShape>): VoxelShape {
        var shape = mergeShapeCache.getIfPresent(shapes)
        if (shape == null) {
            shape = shapes.stream()
                .reduce(Shapes.empty()) { shape1: VoxelShape, shape2: VoxelShape -> Shapes.or(shape1, shape2) }
            mergeShapeCache.put(shapes, shape!!)
        }
        return shape
    }
}