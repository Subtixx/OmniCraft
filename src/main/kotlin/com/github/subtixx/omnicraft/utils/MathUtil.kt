package com.github.subtixx.omnicraft.utils

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3
import kotlin.math.abs
import kotlin.math.sign

@Suppress("unused")
object MathUtil {
    const val PHI: Double = 1.618033988749894
    const val PI: Double = Math.PI
    const val TO_DEG: Double = 57.29577951308232
    const val TO_RAD: Double = 0.017453292519943
    const val SQRT2: Double = 1.414213562373095

    @JvmStatic
    var SIN_TABLE: DoubleArray = DoubleArray(65536)

    init {
        for (i in 0..65535) {
            SIN_TABLE[i] = kotlin.math.sin(i / 65536.0 * 2 * Math.PI)
        }
    }

    @JvmStatic
    fun sin(d: Double): Double {
        return SIN_TABLE[(d.toFloat() * 10430.378f).toInt() and 65535]
    }

    @JvmStatic
    fun cos(d: Double): Double {
        return SIN_TABLE[(d.toFloat() * 10430.378f + 16384.0f).toInt() and 65535]
    }

    /**
     * @param a   The value
     * @param b   The value to approach
     * @param max The maximum step
     * @return the closed value to b no less than max from a
     */
    @JvmStatic
    fun approachLinear(a: Float, b: Float, max: Float): Float {
        return if ((a > b)) (if (a - b < max) b else a - max) else (if (b - a < max) b else a + max)
    }

    /**
     * @param a   The value
     * @param b   The value to approach
     * @param max The maximum step
     * @return the closed value to b no less than max from a
     */
    @JvmStatic
    fun approachLinear(a: Double, b: Double, max: Double): Double {
        return if ((a > b)) (if (a - b < max) b else a - max) else (if (b - a < max) b else a + max)
    }

    /**
     * @param a The first value
     * @param b The second value
     * @param d The interpolation factor, between 0 and 1
     * @return a+(b-a)*d
     */
    @JvmStatic
    fun interpolate(a: Float, b: Float, d: Float): Float {
        return a + (b - a) * d
    }

    /**
     * @param a The first value
     * @param b The second value
     * @param d The interpolation factor, between 0 and 1
     * @return a+(b-a)*d
     */
    @JvmStatic
    fun interpolate(a: Double, b: Double, d: Double): Double {
        return a + (b - a) * d
    }

    /**
     * @param a     The value
     * @param b     The value to approach
     * @param ratio The ratio to reduce the difference by
     * @return a+(b-a)*ratio
     */
    @JvmStatic
    fun approachExp(a: Double, b: Double, ratio: Double): Double {
        return a + (b - a) * ratio
    }

    /**
     * @param a     The value
     * @param b     The value to approach
     * @param ratio The ratio to reduce the difference by
     * @param cap   The maximum amount to advance by
     * @return a+(b-a)*ratio
     */
    @JvmStatic
    fun approachExp(a: Double, b: Double, ratio: Double, cap: Double): Double {
        var d = (b - a) * ratio
        if (abs(d) > cap) {
            d = sign(d) * cap
        }
        return a + d
    }

    /**
     * @param a     The value
     * @param b     The value to approach
     * @param ratio The ratio to reduce the difference by
     * @param c     The value to retreat from
     * @param kick  The difference when a == c
     * @return
     */
    @JvmStatic
    fun retreatExp(a: Double, b: Double, c: Double, ratio: Double, kick: Double): Double {
        val d = (abs(c - a) + kick) * ratio
        if (d > abs(b - a)) {
            return b
        }
        return a + sign(b - a) * d
    }

    /**
     * @param value The value
     * @param min   The min value
     * @param max   The max value
     * @return The clipped value between min and max
     */
    @JvmStatic
    fun clip(value: Double, min: Double, max: Double): Double {
        var mutableValue = value
        if (mutableValue > max) {
            mutableValue = max
        }
        if (mutableValue < min) {
            mutableValue = min
        }
        return mutableValue
    }

    /**
     * @param value The value
     * @param min   The min value
     * @param max   The max value
     * @return The clipped value between min and max
     */
    @JvmStatic
    fun clip(value: Float, min: Float, max: Float): Float {
        var mutableValue = value
        if (mutableValue > max) {
            mutableValue = max
        }
        if (mutableValue < min) {
            mutableValue = min
        }
        return mutableValue
    }

    /**
     * @param value The value
     * @param min   The min value
     * @param max   The max value
     * @return The clipped value between min and max
     */
    @JvmStatic
    fun clip(value: Int, min: Int, max: Int): Int {
        var mutableValue = value
        if (mutableValue > max) {
            mutableValue = max
        }
        if (mutableValue < min) {
            mutableValue = min
        }
        return mutableValue
    }

    /**
     * Maps a value range to another value range.
     *
     * @param valueIn The value to map.
     * @param inMin   The minimum of the input value range.
     * @param inMax   The maximum of the input value range
     * @param outMin  The minimum of the output value range.
     * @param outMax  The maximum of the output value range.
     * @return The mapped value.
     */
    @JvmStatic
    fun map(valueIn: Double, inMin: Double, inMax: Double, outMin: Double, outMax: Double): Double {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
    }

    /**
     * Maps a value range to another value range.
     *
     * @param valueIn The value to map.
     * @param inMin   The minimum of the input value range.
     * @param inMax   The maximum of the input value range
     * @param outMin  The minimum of the output value range.
     * @param outMax  The maximum of the output value range.
     * @return The mapped value.
     */
    @JvmStatic
    fun map(valueIn: Float, inMin: Float, inMax: Float, outMin: Float, outMax: Float): Float {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin
    }

    /**
     * Rounds the number of decimal places based on the given multiplier.<br></br>
     * e.g.<br></br>
     * Input: 17.5245743<br></br>
     * multiplier: 1000<br></br>
     * Output: 17.534<br></br>
     * multiplier: 10<br></br>
     * Output 17.5<br></br><br></br>
     *
     * @param number     The input value.
     * @param multiplier The multiplier.
     * @return The input rounded to a number of decimal places based on the multiplier.
     */
    @JvmStatic
    fun round(number: Double, multiplier: Double): Double {
        return Math.round(number * multiplier) / multiplier
    }

    /**
     * Rounds the number of decimal places based on the given multiplier.<br></br>
     * e.g.<br></br>
     * Input: 17.5245743<br></br>
     * multiplier: 1000<br></br>
     * Output: 17.534<br></br>
     * multiplier: 10<br></br>
     * Output 17.5<br></br><br></br>
     *
     * @param number     The input value.
     * @param multiplier The multiplier.
     * @return The input rounded to a number of decimal places based on the multiplier.
     */
    @JvmStatic
    fun round(number: Float, multiplier: Float): Float {
        return Math.round(number * multiplier) / multiplier
    }

    /**
     * @return `min <= value <= max`
     */
    @JvmStatic
    fun between(min: Double, value: Double, max: Double): Boolean {
        return value in min..max
    }

    @JvmStatic
    fun approachExpI(a: Int, b: Int, ratio: Double): Int {
        val r = Math.round(approachExp(a.toDouble(), b.toDouble(), ratio)).toInt()
        return if (r == a) b else r
    }

    @JvmStatic
    fun retreatExpI(a: Int, b: Int, c: Int, ratio: Double, kick: Int): Int {
        val r = Math.round(retreatExp(a.toDouble(), b.toDouble(), c.toDouble(), ratio, kick.toDouble())).toInt()
        return if (r == a) b else r
    }

    @JvmStatic
    fun floor(d: Double): Int {
        val i = d.toInt()
        return if (d < i.toDouble()) i - 1 else i
    }

    @JvmStatic
    fun floor(f: Float): Int {
        val i = f.toInt()
        return if (f < i.toFloat()) i - 1 else i
    }

    @JvmStatic
    fun ceil(d: Double): Int {
        val i = d.toInt()
        return if (d > i.toDouble()) i + 1 else i
    }

    @JvmStatic
    fun ceil(f: Float): Int {
        val i = f.toInt()
        return if (f > i.toFloat()) i + 1 else i
    }

    @JvmStatic
    fun sqrt(f: Float): Float {
        return kotlin.math.sqrt(f.toDouble()).toFloat()
    }

    @JvmStatic
    fun sqrt(f: Double): Float {
        return kotlin.math.sqrt(f).toFloat()
    }

    @JvmStatic
    fun ceilDiv(num: Int, div: Int): Int {
        return (num + div - 1) / div
    }

    @JvmStatic
    fun roundAway(d: Double): Int {
        return (if (d < 0) kotlin.math.floor(d) else kotlin.math.ceil(d)).toInt()
    }

    @JvmStatic
    fun compare(a: Int, b: Int): Int {
        return a.compareTo(b)
    }

    @JvmStatic
    fun compare(a: Double, b: Double): Int {
        return a.compareTo(b)
    }

    @JvmStatic
    fun min(pos1: Vec3i, pos2: Vec3i): BlockPos {
        return BlockPos(
            kotlin.math.min(pos1.x.toDouble(), pos2.x.toDouble()).toInt(),
            kotlin.math.min(pos1.y.toDouble(), pos2.y.toDouble()).toInt(),
            kotlin.math.min(pos1.z.toDouble(), pos2.z.toDouble()).toInt()
        )
    }

    @JvmStatic
    fun max(pos1: Vec3i, pos2: Vec3i): BlockPos {
        return BlockPos(
            kotlin.math.max(pos1.x.toDouble(), pos2.x.toDouble()).toInt(),
            kotlin.math.max(pos1.y.toDouble(), pos2.y.toDouble()).toInt(),
            kotlin.math.max(pos1.z.toDouble(), pos2.z.toDouble()).toInt()
        )
    }

    @JvmStatic
    fun absSum(pos: BlockPos): Int {
        return (abs(pos.x.toDouble()) + abs(pos.y.toDouble()) + abs(pos.z.toDouble())).toInt()
    }

    @JvmStatic
    fun isAxial(pos: BlockPos): Boolean {
        return if (pos.x == 0) (pos.y == 0 || pos.z == 0) else (pos.y == 0 && pos.z == 0)
    }

    @JvmStatic
    fun toSide(pos: BlockPos): Int {
        val side = getSide(pos)
        return side?.get3DDataValue() ?: -1
    }

    @JvmStatic
    fun getSide(pos: BlockPos): Direction? {
        if (!isAxial(pos)) {
            return null
        }
        if (pos.y < 0) {
            return Direction.DOWN
        }
        if (pos.y > 0) {
            return Direction.UP
        }
        if (pos.z < 0) {
            return Direction.NORTH
        }
        if (pos.z > 0) {
            return Direction.SOUTH
        }
        if (pos.x < 0) {
            return Direction.WEST
        }
        if (pos.x > 0) {
            return Direction.EAST
        }

        return null
    }

    @JvmStatic
    fun isInside(mouseX: Int, mouseY: Int, leftX: Int, topY: Int, width: Int, height: Int): Boolean {
        return mouseX >= leftX && mouseX <= leftX + width && mouseY >= topY && mouseY <= topY + height
    }
}