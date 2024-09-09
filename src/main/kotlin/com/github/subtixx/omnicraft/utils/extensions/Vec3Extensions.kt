@file:Suppress("unused")

package com.github.subtixx.omnicraft.utils.extensions

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

fun Vec3.boundingBox(range: Int): AABB {
    return AABB(
        (x - range),
        (y - range),
        (z - range),
        (x + range + 1),
        (y + range + 1),
        (z + range + 1)
    )
}

fun Vec3.isWithin(x: Double, y: Double, z: Double): Boolean {
    return x >= this.x - x && x <= this.x + x && y >= this.y - y && y <= this.y + y && z >= this.z - z && z <= this.z + z
}

fun Vec3.isWithin(other: Vec3): Boolean {
    return isWithin(other.x, other.y, other.z)
}

fun Vec3.isWithinDistanceCubic(other: BlockPos, range: Int): Boolean {
    val bb = other.boundingBox(range)

    return x in bb.minX..bb.maxX && y in bb.minY..bb.maxY && z in bb.minZ..bb.maxZ
}

fun Vec3.isWithinDistanceCubic(other: Vec3, range: Int): Boolean {
    val bb = other.boundingBox(range)

    return x in bb.minX..bb.maxX && y in bb.minY..bb.maxY && z in bb.minZ..bb.maxZ
}

fun Vec3.isWithinDistanceCylinder(other: BlockPos, range: Int): Boolean {
    val dx = other.x + 0.5 - x
    val dy = other.y + 0.5 - y
    val dz = other.z + 0.5 - z
    return (dx * dx + dz * dz) <= range && dy <= range
}

fun Vec3.writeToNBT(tag: CompoundTag): CompoundTag {
    tag.putDouble("x", x)
    tag.putDouble("y", y)
    tag.putDouble("z", z)
    return tag
}

object Vec3 {
    fun fromNBT(tag: CompoundTag): Vec3 {
        return Vec3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"))
    }
}