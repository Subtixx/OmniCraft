package com.github.subtixx.omnicraft.utils

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode


class Cuboid6 : Copyable<Cuboid6?> {
    var min: Vector3
    var max: Vector3

    @JvmOverloads
    constructor(min: Vector3 = Vector3(), max: Vector3 = Vector3()) {
        this.min = min
        this.max = max
    }

    constructor(min: Vec3i, max: Vec3i) {
        this.min = Vector3.fromVec3i(min)
        this.max = Vector3.fromVec3i(max)
    }

    constructor(aabb: AABB) {
        min = Vector3(aabb.minX, aabb.minY, aabb.minZ)
        max = Vector3(aabb.maxX, aabb.maxY, aabb.maxZ)
    }

    constructor(tag: CompoundTag) : this(
        Vector3.fromNBT(tag.getCompound("min")),
        Vector3.fromNBT(tag.getCompound("max"))
    )

    constructor(cuboid: Cuboid6) {
        min = cuboid.min.copy()
        max = cuboid.max.copy()
    }

    constructor(minx: Double, miny: Double, minz: Double, maxx: Double, maxy: Double, maxz: Double) {
        min = Vector3(minx, miny, minz)
        max = Vector3(maxx, maxy, maxz)
    }

    constructor(minx: Float, miny: Float, minz: Float, maxx: Float, maxy: Float, maxz: Float) {
        min = Vector3(minx.toDouble(), miny.toDouble(), minz.toDouble())
        max = Vector3(maxx.toDouble(), maxy.toDouble(), maxz.toDouble())
    }

    fun aabb(): AABB {
        return AABB(min.x, min.y, min.z, max.x, max.y, max.z)
    }

    fun shape(): VoxelShape {
        return VoxelShapeCache.getShape(this)
    }

    fun writeToNBT(tag: CompoundTag): CompoundTag {
        tag.put("min", min.writeToNBT(CompoundTag()))
        tag.put("max", max.writeToNBT(CompoundTag()))
        return tag
    }

    fun set(minx: Double, miny: Double, minz: Double, maxx: Double, maxy: Double, maxz: Double): Cuboid6 {
        min.set(minx, miny, minz)
        max.set(maxx, maxy, maxz)
        return this
    }

    fun set(min: Vector3, max: Vector3): Cuboid6 {
        return set(min.x, min.y, min.z, max.x, max.y, max.z)
    }

    fun set(min: Vec3i, max: Vec3i): Cuboid6 {
        return set(
            min.x.toDouble(),
            min.y.toDouble(),
            min.z.toDouble(),
            max.x.toDouble(),
            max.y.toDouble(),
            max.z.toDouble()
        )
    }

    fun set(c: Cuboid6): Cuboid6 {
        return set(c.min.x, c.min.y, c.min.z, c.max.x, c.max.y, c.max.z)
    }

    fun set(bb: AABB): Cuboid6 {
        return set(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ)
    }

    fun add(dx: Double, dy: Double, dz: Double): Cuboid6 {
        min.add(dx, dy, dz)
        max.add(dx, dy, dz)
        return this
    }

    fun add(d: Double): Cuboid6 {
        return add(d, d, d)
    }

    fun add(vec: Vector3): Cuboid6 {
        return add(vec.x, vec.y, vec.z)
    }

    fun add(vec: Vec3i): Cuboid6 {
        return add(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
    }

    fun add(pos: BlockPos): Cuboid6 {
        return add(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
    }

    fun subtract(dx: Double, dy: Double, dz: Double): Cuboid6 {
        min.subtract(dx, dy, dz)
        max.subtract(dx, dy, dz)
        return this
    }

    fun subtract(d: Double): Cuboid6 {
        return subtract(d, d, d)
    }

    fun subtract(vec: Vector3): Cuboid6 {
        return subtract(vec.x, vec.y, vec.z)
    }

    fun subtract(vec: Vec3i): Cuboid6 {
        return subtract(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
    }

    fun subtract(vec: Vec3): Cuboid6 {
        return subtract(vec.x, vec.y, vec.z)
    }

    fun subtract(pos: BlockPos): Cuboid6 {
        return subtract(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
    }

    fun expand(dx: Double, dy: Double, dz: Double): Cuboid6 {
        min.subtract(dx, dy, dz)
        max.add(dx, dy, dz)
        return this
    }

    fun expand(d: Double): Cuboid6 {
        return expand(d, d, d)
    }

    fun expand(vec: Vector3): Cuboid6 {
        return expand(vec.x, vec.y, vec.z)
    }

    fun expandSide(side: Direction, amount: Int): Cuboid6 {
        when (side.axisDirection) {
            Direction.AxisDirection.NEGATIVE -> min.add(Vector3.fromVec3i(side.normal).multiply(amount.toDouble()))
            Direction.AxisDirection.POSITIVE -> max.add(Vector3.fromVec3i(side.normal).multiply(amount.toDouble()))
        }
        return this
    }

    fun shrinkSide(side: Direction, amount: Int): Cuboid6 {
        expandSide(side, -amount)
        return this
    }

    fun offset(o: Cuboid6): Cuboid6 {
        min.add(o.min)
        max.add(o.max)
        return this
    }

    fun enclose(minx: Double, miny: Double, minz: Double, maxx: Double, maxy: Double, maxz: Double): Cuboid6 {
        if (min.x > minx) {
            min.x = minx
        }
        if (min.y > miny) {
            min.y = miny
        }
        if (min.z > minz) {
            min.z = minz
        }
        if (max.x < maxx) {
            max.x = maxx
        }
        if (max.y < maxy) {
            max.y = maxy
        }
        if (max.z < maxz) {
            max.z = maxz
        }
        return this
    }

    fun enclose(x: Double, y: Double, z: Double): Cuboid6 {
        return enclose(x, y, z, x, y, z)
    }

    fun enclose(vec: Vector3): Cuboid6 {
        return enclose(vec.x, vec.y, vec.z, vec.x, vec.y, vec.z)
    }

    fun enclose(c: Cuboid6): Cuboid6 {
        return enclose(c.min.x, c.min.y, c.min.z, c.max.x, c.max.y, c.max.z)
    }

    fun contains(x: Double, y: Double, z: Double): Boolean {
        return min.x - 1E-5 <= x && min.y - 1E-5 <= y && min.z - 1E-5 <= z && max.x + 1E-5 >= x && max.y + 1E-5 >= y && max.z + 1E-5 >= z
    }

    fun contains(vec: Vector3): Boolean {
        return contains(vec.x, vec.y, vec.z)
    }

    fun intersects(b: Cuboid6): Boolean {
        return max.x - 1E-5 > b.min.x && max.y - 1E-5 > b.min.y && max.z - 1E-5 > b.min.z && b.max.x - 1E-5 > min.x && b.max.y - 1E-5 > min.y && b.max.z - 1E-5 > min.z
    }

    fun volume(): Double {
        return (max.x - min.x + 1) * (max.y - min.y + 1) * (max.z - min.z + 1)
    }

    fun center(): Vector3 {
        return min.copy().add(max).multiply(0.5)
    }

    fun getSideSize(side: Direction): Double {
        return when (side.axis) {
            Direction.Axis.X -> (max.x - min.x) + 1
            Direction.Axis.Y -> (max.y - min.y) + 1
            Direction.Axis.Z -> (max.z - min.z) + 1
        }
    }

    fun getSide(side: Int): Double {
        return when (side) {
            0 -> min.y
            1 -> max.y
            2 -> min.z
            3 -> max.z
            4 -> min.x
            5 -> max.x
            else -> 0.0
        }
    }

    fun getSide(side: Direction): Double {
        return getSide(side.ordinal)
    }

    fun setSide(side: Int, d: Double): Cuboid6 {
        when (side) {
            0 -> min.y = d
            1 -> max.y = d
            2 -> min.z = d
            3 -> max.z = d
            4 -> min.x = d
            5 -> max.x = d
        }
        return this
    }

    fun setSide(side: Direction, d: Double): Cuboid6 {
        return setSide(side.ordinal, d)
    }

    override fun hashCode(): Int {
        var i = java.lang.Double.doubleToLongBits(min.x)
        var j = (i xor (i ushr 32)).toInt()
        i = java.lang.Double.doubleToLongBits(min.y)
        j = 31 * j + (i xor (i ushr 32)).toInt()
        i = java.lang.Double.doubleToLongBits(min.z)
        j = 31 * j + (i xor (i ushr 32)).toInt()
        i = java.lang.Double.doubleToLongBits(max.x)
        j = 31 * j + (i xor (i ushr 32)).toInt()
        i = java.lang.Double.doubleToLongBits(max.y)
        j = 31 * j + (i xor (i ushr 32)).toInt()
        i = java.lang.Double.doubleToLongBits(max.z)
        j = 31 * j + (i xor (i ushr 32)).toInt()
        return j
    }

    override fun equals(obj: Any?): Boolean {
        if (super.equals(obj)) {
            return true
        }
        if (obj !is Cuboid6) {
            return false
        }
        val c = obj
        return min.equals(c.min) && max.equals(c.max)
    }

    fun equalsT(c: Cuboid6): Boolean {
        return min.equalsT(c.min) && max.equalsT(c.max)
    }

    override fun copy(): Cuboid6 {
        return Cuboid6(this)
    }

    override fun toString(): String {
        val cont = MathContext(4, RoundingMode.HALF_UP)
        return "Cuboid: (" + BigDecimal(min.x, cont) + ", " + BigDecimal(min.y, cont) + ", " + BigDecimal(
            min.z,
            cont
        ) + ") -> (" + BigDecimal(max.x, cont) + ", " + BigDecimal(max.y, cont) + ", " + BigDecimal(max.z, cont) + ")"
    }

    companion object {
        var full: Cuboid6 = Cuboid6(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
    }
}