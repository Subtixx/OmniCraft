package com.github.subtixx.omnicraft.utils

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.abs

abstract class Vec3Extension {
    companion object {
        fun BoundingBox(torch: BlockPos, range: Int): AABB {
            return AABB(
                (torch.x - range).toDouble(),
                (torch.y - range).toDouble(),
                (torch.z - range).toDouble(),
                (torch.x + range + 1).toDouble(),
                (torch.y + range + 1).toDouble(),
                (torch.z + range + 1).toDouble()
            )
        }

        fun DistCubic(first: Vec3, second: BlockPos, range: Int): Boolean {
            return DistCubic(first.x, first.y, first.z, second, range)
        }

        fun DistCubic(first: BlockPos, second: BlockPos, range: Int): Boolean {
            return DistCubic(first.x.toDouble(), first.y.toDouble(), first.z.toDouble(), second, range)
        }

        fun DistCubic(x: Double, y: Double, z: Double, torch: BlockPos, range: Int): Boolean {
            // a range of 0 is effectively a 1x1x1 block volume, the torch itself.
            val minX = (torch.x - range).toDouble()
            val minY = (torch.y - range).toDouble()
            val minZ = (torch.z - range).toDouble()
            val maxX = (torch.x + range + 1).toDouble()
            val maxY = (torch.y + range + 1).toDouble()
            val maxZ = (torch.z + range + 1).toDouble()

            return x in minX..maxX &&
                    y in minY..maxY &&
                    z in minZ..maxZ
        }

        fun DistCylinder(x: Double, y: Double, z: Double, torch: BlockPos, range: Int): Boolean {
            val dx = torch.x + 0.5 - x
            val dy = abs(torch.y + 0.5 - y)
            val dz = torch.z + 0.5 - z
            return (dx * dx + dz * dz) <= range && dy <= range
        }
    }
}