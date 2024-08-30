package com.github.subtixx.omnicraft.config

import net.minecraft.core.Direction
import net.minecraft.world.item.DyeColor

enum class LineColor(private val color: DyeColor) {
    ORANGE(DyeColor.ORANGE),
    MAGENTA(DyeColor.MAGENTA),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE),
    YELLOW(DyeColor.YELLOW),
    LIME(DyeColor.LIME),
    PINK(DyeColor.PINK),
    CYAN(DyeColor.CYAN),
    PURPLE(DyeColor.PURPLE),
    BLUE(DyeColor.BLUE),
    BROWN(DyeColor.BROWN),
    GREEN(DyeColor.GREEN),
    RED(DyeColor.RED);

    companion object {
        fun byId(id: Int): DyeColor {
            return entries[id].color
        }

        fun byAxis(axis: Direction.Axis): DyeColor {
            return when (axis) {
                Direction.Axis.X -> {
                    DyeColor.RED
                }

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
