package com.github.subtixx.omnicraft.utils

import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import org.joml.Vector4f
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.math.acos
import kotlin.math.sqrt


class Vector3 : Copyable<Vector3?> {
    //@formatter:on
    var x: Double = 0.0
    var y: Double = 0.0
    var z: Double = 0.0

    constructor()

    constructor(d: Double, d1: Double, d2: Double) {
        x = d
        y = d1
        z = d2
    }

    constructor(vec: Vector3) {
        x = vec.x
        y = vec.y
        z = vec.z
    }

    constructor(da: DoubleArray) : this(da[0], da[1], da[2])

    constructor(fa: FloatArray) : this(fa[0].toDouble(), fa[1].toDouble(), fa[2].toDouble())

    constructor(vec: Vec3) {
        x = vec.x
        y = vec.y
        z = vec.z
    }

    fun vec3(): Vec3 {
        return Vec3(x, y, z)
    }

    fun pos(): BlockPos {
        return BlockPos(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z))
    }

    fun writeToNBT(tag: CompoundTag): CompoundTag {
        tag.putDouble("x", x)
        tag.putDouble("y", y)
        tag.putDouble("z", z)
        return tag
    }

    fun vector3f(): Vector3f {
        return Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun vector4f(): Vector4f {
        return Vector4f(x.toFloat(), y.toFloat(), z.toFloat(), 1f)
    }

    fun toArrayD(): DoubleArray {
        return doubleArrayOf(x, y, z)
    }

    fun toArrayF(): FloatArray {
        return floatArrayOf(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun set(x1: Double, y1: Double, z1: Double): Vector3 {
        x = x1
        y = y1
        z = z1
        return this
    }

    fun set(d: Double): Vector3 {
        return set(d, d, d)
    }

    fun set(vec: Vector3): Vector3 {
        return set(vec.x, vec.y, vec.z)
    }

    fun set(vec: Vec3i): Vector3 {
        return set(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
    }

    fun set(da: DoubleArray): Vector3 {
        return set(da[0], da[1], da[2])
    }

    fun set(fa: FloatArray): Vector3 {
        return set(fa[0].toDouble(), fa[1].toDouble(), fa[2].toDouble())
    }

    fun add(dx: Double, dy: Double, dz: Double): Vector3 {
        x += dx
        y += dy
        z += dz
        return this
    }

    fun add(d: Double): Vector3 {
        return add(d, d, d)
    }

    fun add(vec: Vector3): Vector3 {
        return add(vec.x, vec.y, vec.z)
    }

    fun add(vec: Vec3): Vector3 {
        return add(vec.x, vec.y, vec.z)
    }

    // TODO Move to use Vec3i
    fun add(pos: BlockPos): Vector3 {
        return add(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
    }

    fun subtract(dx: Double, dy: Double, dz: Double): Vector3 {
        x -= dx
        y -= dy
        z -= dz
        return this
    }

    fun subtract(d: Double): Vector3 {
        return subtract(d, d, d)
    }

    fun subtract(vec: Vector3): Vector3 {
        return subtract(vec.x, vec.y, vec.z)
    }

    fun subtract(vec: Vec3): Vector3 {
        return subtract(vec.x, vec.y, vec.z)
    }

    fun subtract(pos: BlockPos): Vector3 {
        return subtract(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
    }

    fun multiply(fx: Double, fy: Double, fz: Double): Vector3 {
        x *= fx
        y *= fy
        z *= fz
        return this
    }

    fun multiply(f: Double): Vector3 {
        return multiply(f, f, f)
    }

    fun multiply(f: Vector3): Vector3 {
        return multiply(f.x, f.y, f.z)
    }

    fun divide(fx: Double, fy: Double, fz: Double): Vector3 {
        x /= fx
        y /= fy
        z /= fz
        return this
    }

    fun divide(f: Double): Vector3 {
        return divide(f, f, f)
    }

    fun divide(vec: Vector3): Vector3 {
        return divide(vec.x, vec.y, vec.z)
    }

    fun divide(pos: BlockPos): Vector3 {
        return divide(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
    }

    fun floor(): Vector3 {
        x = MathHelper.floor(x).toDouble()
        y = MathHelper.floor(y).toDouble()
        z = MathHelper.floor(z).toDouble()
        return this
    }

    fun ceil(): Vector3 {
        x = MathHelper.ceil(x).toDouble()
        y = MathHelper.ceil(y).toDouble()
        z = MathHelper.ceil(z).toDouble()
        return this
    }

    fun mag(): Double {
        return sqrt(x * x + y * y + z * z)
    }

    fun magSquared(): Double {
        return x * x + y * y + z * z
    }

    fun negate(): Vector3 {
        x = -x
        y = -y
        z = -z
        return this
    }

    fun normalize(): Vector3 {
        val d = mag()
        if (d != 0.0) {
            multiply(1 / d)
        }
        return this
    }

    fun distance(other: Vector3): Double {
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z

        return sqrt(dx * dx + dy * dy + dz * dz)
    }

    fun distanceSquared(other: Vector3): Double {
        val dx = x - other.x
        val dy = y - other.y
        val dz = z - other.z

        return dx * dx + dy * dy + dz * dz
    }

    fun dotProduct(x1: Double, y1: Double, z1: Double): Double {
        return x1 * x + y1 * y + z1 * z
    }

    fun dotProduct(vec: Vector3): Double {
        var d = vec.x * x + vec.y * y + vec.z * z

        if (d > 1 && d < 1.00001) {
            d = 1.0
        } else if (d < -1 && d > -1.00001) {
            d = -1.0
        }
        return d
    }

    fun crossProduct(vec: Vector3): Vector3 {
        val d = y * vec.z - z * vec.y
        val d1 = z * vec.x - x * vec.z
        val d2 = x * vec.y - y * vec.x
        x = d
        y = d1
        z = d2
        return this
    }

    fun perpendicular(): Vector3 {
        if (z == 0.0) {
            return zCrossProduct()
        }
        return xCrossProduct()
    }

    fun xCrossProduct(): Vector3 {
        val d = z
        val d1 = -y
        x = 0.0
        y = d
        z = d1
        return this
    }

    fun zCrossProduct(): Vector3 {
        val d = y
        val d1 = -x
        x = d
        y = d1
        z = 0.0
        return this
    }

    fun yCrossProduct(): Vector3 {
        val d = -z
        val d1 = x
        x = d
        y = 0.0
        z = d1
        return this
    }

    fun scalarProject(b: Vector3): Double {
        val l = b.mag()
        return if (l == 0.0) 0.0 else dotProduct(b) / l
    }

    fun project(b: Vector3): Vector3 {
        val l = b.magSquared()
        if (l == 0.0) {
            set(0.0, 0.0, 0.0)
            return this
        }
        val m = dotProduct(b) / l
        set(b).multiply(m)
        return this
    }

    fun angle(vec: Vector3): Double {
        return acos(copy().normalize().dotProduct(vec.copy().normalize()))
    }

    fun YZintercept(end: Vector3, px: Double): Vector3? {
        val dx = end.x - x
        val dy = end.y - y
        val dz = end.z - z

        if (dx == 0.0) {
            return null
        }

        val d = (px - x) / dx
        if (MathHelper.between(-1E-5, d, 1E-5)) {
            return this
        }

        if (!MathHelper.between(0.0, d, 1.0)) {
            return null
        }

        x = px
        y += d * dy
        z += d * dz
        return this
    }

    fun XZintercept(end: Vector3, py: Double): Vector3? {
        val dx = end.x - x
        val dy = end.y - y
        val dz = end.z - z

        if (dy == 0.0) {
            return null
        }

        val d = (py - y) / dy
        if (MathHelper.between(-1E-5, d, 1E-5)) {
            return this
        }

        if (!MathHelper.between(0.0, d, 1.0)) {
            return null
        }

        x += d * dx
        y = py
        z += d * dz
        return this
    }

    fun XYintercept(end: Vector3, pz: Double): Vector3? {
        val dx = end.x - x
        val dy = end.y - y
        val dz = end.z - z

        if (dz == 0.0) {
            return null
        }

        val d = (pz - z) / dz
        if (MathHelper.between(-1E-5, d, 1E-5)) {
            return this
        }

        if (!MathHelper.between(0.0, d, 1.0)) {
            return null
        }

        x += d * dx
        y += d * dy
        z = pz
        return this
    }

    val isZero: Boolean
        get() = x == 0.0 && y == 0.0 && z == 0.0

    val isAxial: Boolean
        get() = if (x == 0.0) (y == 0.0 || z == 0.0) else (y == 0.0 && z == 0.0)

    fun getSide(side: Int): Double {
        when (side) {
            0, 1 -> return y
            2, 3 -> return z
            4, 5 -> return x
        }
        throw IndexOutOfBoundsException("Switch Falloff")
    }

    fun setSide(s: Int, v: Double): Vector3 {
        when (s) {
            0, 1 -> y = v
            2, 3 -> z = v
            4, 5 -> x = v
            else -> throw IndexOutOfBoundsException("Switch Falloff")
        }
        return this
    }

    override fun hashCode(): Int {
        var j = java.lang.Double.doubleToLongBits(x)
        var i = (j xor (j ushr 32)).toInt()
        j = java.lang.Double.doubleToLongBits(y)
        i = 31 * i + (j xor (j ushr 32)).toInt()
        j = java.lang.Double.doubleToLongBits(z)
        i = 31 * i + (j xor (j ushr 32)).toInt()
        return i
    }

    override fun equals(o: Any?): Boolean {
        if (super.equals(o)) {
            return true
        }
        if (o !is Vector3) {
            return false
        }
        val v = o
        return x == v.x && y == v.y && z == v.z
    }

    /**
     * Equals method with tolerance
     *
     * @return true if this is equal to v within +-1E-5
     */
    fun equalsT(v: Vector3): Boolean {
        return MathHelper.between(x - 1E-5, v.x, x + 1E-5) && MathHelper.between(
            y - 1E-5,
            v.y,
            y + 1E-5
        ) && MathHelper.between(z - 1E-5, v.z, z + 1E-5)
    }

    override fun copy(): Vector3 {
        return Vector3(this)
    }

    override fun toString(): String {
        val cont = MathContext(4, RoundingMode.HALF_UP)
        return "Vector3(" + BigDecimal(x, cont) + ", " + BigDecimal(y, cont) + ", " + BigDecimal(z, cont) + ")"
    }

    fun `$tilde`(): Vector3 {
        return normalize()
    }

    fun `unary_$tilde`(): Vector3 {
        return normalize()
    }

    fun `$plus`(v: Vector3): Vector3 {
        return add(v)
    }

    fun `$minus`(v: Vector3): Vector3 {
        return subtract(v)
    }

    fun `$times`(d: Double): Vector3 {
        return multiply(d)
    }

    fun `$div`(d: Double): Vector3 {
        return multiply(1 / d)
    }

    fun `$times`(v: Vector3): Vector3 {
        return crossProduct(v)
    }

    fun `$dot$times`(v: Vector3): Double {
        return dotProduct(v)
    }

    companion object {
        val ZERO: Vector3 = Vector3(0.0, 0.0, 0.0)
        val CENTER: Vector3 = Vector3(0.5, 0.5, 0.5)

        //@formatter:off
        val ONE: Vector3 = Vector3(1.0, 1.0, 1.0)
        val X_POS: Vector3 = Vector3(1.0, 0.0, 0.0)
        val X_NEG: Vector3 = Vector3(-1.0, 0.0, 0.0)
        val Y_POS: Vector3 = Vector3(0.0, 1.0, 0.0)
        val Y_NEG: Vector3 = Vector3(0.0, -1.0, 0.0)
        val Z_POS: Vector3 = Vector3(0.0, 0.0, 1.0)
        val Z_NEG: Vector3 = Vector3(0.0, 0.0, -1.0)

        fun fromBlockPos(pos: BlockPos): Vector3 {
            return fromVec3i(pos)
        }

        fun fromVec3i(pos: Vec3i): Vector3 {
            return Vector3(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
        }

        fun fromBlockPosCenter(pos: BlockPos): Vector3 {
            return fromBlockPos(pos).add(0.5)
        }

        fun fromEntity(e: Entity): Vector3 {
            return Vector3(e.position())
        }

        fun fromTile(tile: BlockEntity): Vector3 {
            return fromBlockPos(tile.blockPos)
        }

        fun fromTileCenter(tile: BlockEntity): Vector3 {
            return fromTile(tile).add(0.5)
        }

        fun fromAxes(da: DoubleArray): Vector3 {
            return Vector3(da[2], da[0], da[1])
        }

        fun fromAxes(fa: FloatArray): Vector3 {
            return Vector3(fa[2].toDouble(), fa[0].toDouble(), fa[1].toDouble())
        }

        fun fromArray(da: DoubleArray): Vector3 {
            return Vector3(da[0], da[1], da[2])
        }

        fun fromArray(fa: FloatArray): Vector3 {
            return Vector3(fa[0].toDouble(), fa[1].toDouble(), fa[2].toDouble())
        }

        fun fromNBT(tag: CompoundTag): Vector3 {
            return Vector3(tag.getDouble("x"), tag.getDouble("y"), tag.getDouble("z"))
        }
    }}