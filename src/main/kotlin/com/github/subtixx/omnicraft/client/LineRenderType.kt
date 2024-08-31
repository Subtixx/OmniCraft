package com.github.subtixx.omnicraft.client

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.renderer.RenderType
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import java.util.*

@OnlyIn(Dist.CLIENT)
class LineRenderType(
    nameIn: String,
    formatIn: VertexFormat,
    drawMode: VertexFormat.Mode,
    bufferSizeIn: Int,
    useDelegateIn: Boolean,
    needsSortingIn: Boolean,
    setupTaskIn: Runnable,
    clearTaskIn: Runnable
) :
    RenderType(nameIn, formatIn, drawMode, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn) {
    companion object {
        fun lineRenderType(lineWidth: Float): RenderType {
            return create(
                "lines_no_depth",
                DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 256, false, false,
                CompositeState.builder().setShaderState(RENDERTYPE_LINES_SHADER)
                    .setLineState(LineStateShard(OptionalDouble.of(lineWidth.toDouble())))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(NO_TRANSPARENCY)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .createCompositeState(false)
            )
        }

        fun translucentLineRenderType(lineWidth: Float): RenderType {
            return create(
                "translucent_lines",
                DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false,
                CompositeState.builder().setShaderState(RENDERTYPE_LINES_SHADER)
                    .setLineState(LineStateShard(OptionalDouble.of(lineWidth.toDouble())))
                    .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(ITEM_ENTITY_TARGET)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .setCullState(NO_CULL)
                    .setDepthTestState(NO_DEPTH_TEST)
                    .createCompositeState(false)
            )
        }
    }
}
