package com.github.subtixx.omnicraft.menu.block.energy

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.menu.OmnicraftBaseContainerScreen
import com.github.subtixx.omnicraft.utils.NumberUtil
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import java.util.*

@OnlyIn(Dist.CLIENT)
abstract class EnergyStorageContainerScreen<T> @JvmOverloads constructor(
    menu: T,
    inventory: Inventory,
    titleComponent: Component,
    protected val energyIndicatorBarTooltipComponentID: String? = null,
    protected val TEXTURE: ResourceLocation = ResourceLocation.fromNamespaceAndPath(
        Omnicraft.ID,
        "textures/gui/container/generic_energy.png"
    )
) :
    OmnicraftBaseContainerScreen<T>(
        menu,
        inventory,
        titleComponent
    ) where T : AbstractContainerMenu, T : IEnergyStorageMenu? {
    protected var energyMeterX: Int = 8
    protected var energyMeterY: Int = 17
    protected var energyMeterWidth: Int = 16
    protected var energyMeterHeight: Int = 52

    protected var energyMeterU: Int = 176
    protected var energyMeterV: Int = 0

    constructor(
        menu: T,
        inventory: Inventory,
        titleComponent: Component,
        texture: ResourceLocation
    ) : this(menu, inventory, titleComponent, null, texture)

    override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        val x: Int = (width - imageWidth) / 2
        val y: Int = (height - imageHeight) / 2

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight)
        renderEnergyMeter(guiGraphics, x, y)
        renderEnergyIndicatorBar(guiGraphics, x, y)
    }

    protected fun renderEnergyMeter(guiGraphics: GuiGraphics, x: Int, y: Int) {
        val pos: Int = menu.getScaledEnergyMeterPos(energyMeterHeight)
        guiGraphics.blit(
            TEXTURE, x + energyMeterX, y + energyMeterY + energyMeterHeight - pos, energyMeterU,
            energyMeterV + energyMeterHeight - pos, energyMeterWidth, pos
        )
    }

    protected fun renderEnergyIndicatorBar(guiGraphics: GuiGraphics, x: Int, y: Int) {
        val pos: Int = menu.getScaledEnergyIndicatorBarPos(energyMeterHeight)
        if (pos > 0) guiGraphics.blit(
            TEXTURE, x + energyMeterX, y + energyMeterY + energyMeterHeight - pos, energyMeterU,
            energyMeterV + energyMeterHeight, energyMeterWidth, 1
        )
    }

    override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(guiGraphics, mouseX, mouseY, delta)

        renderTooltip(guiGraphics, mouseX, mouseY)
    }

    override fun renderTooltip(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
        super.renderTooltip(guiGraphics, mouseX, mouseY)

        if (isHovering(energyMeterX, energyMeterY, energyMeterWidth, energyMeterHeight, mouseX.toDouble(),
                mouseY.toDouble()
            )) {
            val components: MutableList<Component> = ArrayList(2)
            components.add(
                Component.translatable(
                    "tooltip.omnicraft.energy_meter.content.txt",
                    NumberUtil.formatNumber(menu.energy),
                    NumberUtil.formatNumber(menu.capacity)
                )
            )
            if (menu.energyIndicatorBarValue > 0 && energyIndicatorBarTooltipComponentID != null) {
                components.add(
                    Component.translatable(
                        energyIndicatorBarTooltipComponentID,
                        NumberUtil.formatNumber(menu.energyIndicatorBarValue)
                    ).withStyle(ChatFormatting.YELLOW)
                )
            }

            guiGraphics.renderTooltip(font, components, Optional.empty(), mouseX, mouseY)
        }
    }
}