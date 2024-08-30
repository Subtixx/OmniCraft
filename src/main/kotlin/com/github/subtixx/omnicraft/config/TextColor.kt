package com.github.subtixx.omnicraft.config

import net.minecraft.core.Direction
import net.minecraft.world.item.DyeColor
import java.util.*

enum class TextColor(private val color: DyeColor) {
    WHITE(DyeColor.WHITE),
    ORANGE(DyeColor.ORANGE),
    MAGENTA(DyeColor.MAGENTA),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE),
    YELLOW(DyeColor.YELLOW),
    LIME(DyeColor.LIME),
    PINK(DyeColor.PINK),
    GRAY(DyeColor.GRAY),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY),
    CYAN(DyeColor.CYAN),
    PURPLE(DyeColor.PURPLE),
    BLUE(DyeColor.BLUE),
    BROWN(DyeColor.BROWN),
    GREEN(DyeColor.GREEN),
    RED(DyeColor.RED),
    BLACK(DyeColor.BLACK);

    companion object {
        fun byId(id: Int): DyeColor {
            return TextColor.entries[id].color
        }

        fun byAxis(axis: Direction.Axis): DyeColor {
            return when (axis) {
                Direction.Axis.Y -> {
                    DyeColor.GREEN
                }

                Direction.Axis.Z -> {
                    DyeColor.BLUE
                }

                else -> {
                    DyeColor.RED
                }
            }
        }
    }
}
