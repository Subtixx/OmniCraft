package com.github.subtixx.omnicraft.menu

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferUploader
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.InventoryMenu
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack
import kotlin.math.min

@OnlyIn(Dist.CLIENT)
abstract class OmnicraftBaseContainerScreen<T : AbstractContainerMenu>(
    menu: T,
    inventory: Inventory,
    titleComponent: Component
) :
    AbstractContainerScreen<T>(menu, inventory, titleComponent) {
    protected fun renderFluidMeterContent(
        guiGraphics: GuiGraphics, fluidStack: FluidStack, tankCapacity: Int, x: Int, y: Int,
        w: Int, h: Int
    ) {
        RenderSystem.enableBlend()
        guiGraphics.pose().pushPose()

        guiGraphics.pose().translate(x.toFloat(), y.toFloat(), 0f)

        renderFluidStack(guiGraphics, fluidStack, tankCapacity, w, h)

        guiGraphics.pose().popPose()
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        RenderSystem.disableBlend()
    }

    private fun renderFluidStack(guiGraphics: GuiGraphics, fluidStack: FluidStack, tankCapacity: Int, w: Int, h: Int) {
        if (fluidStack.isEmpty) return

        val fluid = fluidStack.fluid
        val fluidTypeExtensions = IClientFluidTypeExtensions.of(fluid)
        val stillFluidImageId = fluidTypeExtensions.getStillTexture(fluidStack)
        val stillFluidSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(
            stillFluidImageId
        )

        val fluidColorTint = fluidTypeExtensions.getTintColor(fluidStack)

        val fluidMeterPos = if (tankCapacity == -1 || (fluidStack.amount > 0 && fluidStack.amount == tankCapacity)) {
            0
        } else if (fluidStack.amount > 0 && tankCapacity > 0) {
            min(
                fluidStack.amount.toDouble(),
                (tankCapacity - 1).toDouble()
            ).toInt()
        }else{
            h
        }

        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS)

        RenderSystem.setShader { GameRenderer.getPositionTexShader() }
        RenderSystem.setShaderColor(
            (fluidColorTint shr 16 and 0xFF) / 255f,
            (fluidColorTint shr 8 and 0xFF) / 255f, (fluidColorTint and 0xFF) / 255f,
            (fluidColorTint shr 24 and 0xFF) / 255f
        )

        val mat = guiGraphics.pose().last().pose()

        var yOffset = h
        while (yOffset > fluidMeterPos) {
            var xOffset = 0
            while (xOffset < w) {
                val width = min((w - xOffset).toDouble(), 16.0).toInt()
                val height = min((yOffset - fluidMeterPos).toDouble(), 16.0).toInt()

                val u0 = stillFluidSprite.u0
                var u1 = stillFluidSprite.u1
                var v0 = stillFluidSprite.v0
                val v1 = stillFluidSprite.v1
                u1 = u1 - ((16 - width) / 16f * (u1 - u0))
                v0 = v0 - ((16 - height) / 16f * (v0 - v1))

                val tesselator = Tesselator.getInstance()
                val bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX)
                bufferBuilder.addVertex(mat, xOffset.toFloat(), yOffset.toFloat(), 0f).setUv(u0, v1)
                bufferBuilder.addVertex(mat, (xOffset + width).toFloat(), yOffset.toFloat(), 0f).setUv(u1, v1)
                bufferBuilder.addVertex(mat, (xOffset + width).toFloat(), (yOffset - height).toFloat(), 0f)
                    .setUv(u1, v0)
                bufferBuilder.addVertex(mat, xOffset.toFloat(), (yOffset - height).toFloat(), 0f).setUv(u0, v0)
                BufferUploader.drawWithShader(bufferBuilder.buildOrThrow())
                xOffset += 16
            }
            yOffset -= 16
        }
    }
}