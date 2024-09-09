@file:Suppress("unused")

package com.github.subtixx.omnicraft.utils.extensions

import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

fun AABB.writeToNBT(tag: CompoundTag): CompoundTag {
    val min = Vec3(minX, minY, minZ)
    val max = Vec3(maxX, maxY, maxZ)
    tag.put("min", min.writeToNBT(CompoundTag()))
    tag.put("max", max.writeToNBT(CompoundTag()))
    return tag
}


fun AABB.expand(amt: Double): AABB {
    return inflate(amt, amt, amt)
}

fun AABB.translate(x: Double, y: Double, z: Double): AABB {
    return move(x, y, z)
}

fun AABB.center(): Vec3 {
    return Vec3((minX + maxX) / 2.0, (minY + maxY) / 2.0, (minZ + maxZ) / 2.0)
}

fun AABB.volume(): Double {
    return (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1)
}

fun AABB.offset(dx: Double, dy: Double, dz: Double): AABB {
    return AABB(
        minX + dx,
        minY + dy,
        minZ + dz,
        maxX + dx,
        maxY + dy,
        maxZ + dz
    )
}

fun AABB.offset(vec: Vec3): AABB {
    return AABB(
        minX + vec.x,
        minY + vec.y,
        minZ + vec.z,
        maxX + vec.x,
        maxY + vec.y,
        maxZ + vec.z
    )
}

fun AABB.offset(other: AABB): AABB {
    return AABB(
        minX + other.minX,
        minY + other.minY,
        minZ + other.minZ,
        maxX + other.maxX,
        maxY + other.maxY,
        maxZ + other.maxZ
    )
}

fun AABB.rotate(facing: Direction): AABB {
    return when (facing) {
        Direction.UP -> this
        Direction.DOWN -> AABB(this.minX, 1 - this.minY, this.minZ, this.maxX, 1 - this.maxY, this.maxZ)
        Direction.EAST -> AABB(this.minY, 1 - this.minZ, 1 - this.minX, this.maxY, 1 - this.maxZ, 1 - this.maxX)
        Direction.WEST -> AABB(1 - this.minY, 1 - this.minZ, this.minX, 1 - this.maxY, 1 - this.maxZ, this.maxX)
        Direction.NORTH -> AABB(1 - this.minX, 1 - this.minZ, 1 - this.minY, 1 - this.maxX, 1 - this.maxZ, 1 - this.maxY)
        Direction.SOUTH -> AABB(this.minX, 1 - this.minZ, this.minY, this.maxX, 1 - this.maxZ, this.maxY)
    }
}