package com.github.subtixx.omnicraft.utils

import net.minecraft.util.FastColor
import net.minecraft.world.item.DyeColor
import kotlin.math.abs

class Color(
    /**
     * Value between 0.0 and 1.0
     */
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float
) {
    constructor(packedColor: Int) : this(
        (packedColor shr 16 and 0xFF) / 255f,
        (packedColor shr 8 and 0xFF) / 255f,
        (packedColor and 0xFF) / 255f,
        (packedColor shr 24 and 0xFF) / 255f
    )

    fun getClosestDyeColor(): DyeColor {
        var bestColor = DyeColor.WHITE
        var bestDifference = 1024

        for (color in DyeColor.entries) {
            val iColor = color.textureDiffuseColor

            val iRed = FastColor.ARGB32.red(iColor)
            val iGreen = FastColor.ARGB32.green(iColor)
            val iBlue = FastColor.ARGB32.blue(iColor)

            val difference =
                (abs((red - iRed).toDouble()) + abs((green - iGreen).toDouble()) + abs((blue - iBlue).toDouble())).toInt()

            if (difference < bestDifference) {
                bestColor = color
                bestDifference = difference
            }
        }

        return bestColor
    }
}