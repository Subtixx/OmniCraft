package com.github.subtixx.omnicraft.client.renderer

import com.mojang.blaze3d.vertex.VertexConsumer
import net.neoforged.neoforge.client.model.pipeline.VertexConsumerWrapper

internal class ForcedAlphaVertexConsumer(
    wrapped: VertexConsumer,
    private val alpha: Int
) :
    VertexConsumerWrapper(wrapped) {
    override fun setColor(r: Int, g: Int, b: Int, a: Int): VertexConsumer {
        return parent.setColor(r, g, b, (a * this.alpha) / 0xFF)
    }
}