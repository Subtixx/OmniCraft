package com.github.subtixx.omnicraft.utils.gui

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import java.util.*

class GuiButton(
    val id: Int,
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    private val normalTexture: ButtonTexture? = null,
    private val activeTexture: ButtonTexture? = null,
    private val hoveredTexture: ButtonTexture? = null,
    private val isHovering: (GuiButton, Int, Int) -> Boolean = { _, _, _ -> false },
    private val isDisabled: (GuiButton) -> Boolean = { false },
    private val clickEvent: (GuiButton) -> Unit = {},
    private val isActive: (GuiButton) -> Boolean = { false },
    private val tooltip: MutableList<Component>? = null,
    private val font: Font = Minecraft.getInstance().font
) {
    fun render(guiGraphics: GuiGraphics, mainX:Int, mainY:Int, mouseX: Int, mouseY: Int) {
        val texture = when {
            isDisabled(this) -> normalTexture
            isActive(this) -> activeTexture
            isHovering(this, mouseX, mouseY) -> hoveredTexture
            else -> normalTexture
        }
        if (texture == null) return
        guiGraphics.blit(texture.texture, mainX + x, mainY + y, texture.textureU, texture.textureV, width, height)
    }

    fun renderTooltip(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        if (tooltip == null) return
        if (!isHovering(this, mouseX, mouseY)) return

        guiGraphics.renderTooltip(font, tooltip, Optional.empty(), mouseX, mouseY)
    }

    fun clicked(mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        if (mouseButton != 0) return false
        if (!isHovering(this, mouseX, mouseY)) return false
        if (isDisabled(this)) return false

        clickEvent(this)
        return true
    }
}