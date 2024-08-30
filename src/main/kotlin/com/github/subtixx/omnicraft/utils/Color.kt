package com.github.subtixx.omnicraft.utils

import com.github.subtixx.omnicraft.config.LineColor
import com.github.subtixx.omnicraft.config.TextColor
import net.minecraft.world.item.DyeColor

class Color {

    companion object {
        public fun getLineColor(colorIndex: Int): DyeColor {
            if (colorIndex >= LineColor.entries.size) {
                return LineColor.byId(0)
            }

            val selectedColor = LineColor.byId(colorIndex)

            return selectedColor
        }

        public fun getTextColor(colorIndex: Int): DyeColor {
            if (colorIndex >= TextColor.entries.size) {
                return TextColor.byId(0)
            }

            val selectedColor = TextColor.byId(colorIndex)

            return selectedColor
        }
    }
}