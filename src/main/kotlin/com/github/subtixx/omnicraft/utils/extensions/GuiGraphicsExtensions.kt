package com.github.subtixx.omnicraft.utils.extensions

import com.github.subtixx.omnicraft.utils.Color
import net.minecraft.client.gui.GuiGraphics

fun GuiGraphics.setColor(color: Color) {
    setColor(color.red, color.green, color.blue, color.alpha)
}