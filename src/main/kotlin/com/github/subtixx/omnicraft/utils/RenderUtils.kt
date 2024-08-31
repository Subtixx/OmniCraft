package com.github.subtixx.omnicraft.utils

import com.github.subtixx.omnicraft.client.LineRenderType
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Camera
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderBuffers
import net.minecraft.client.renderer.RenderType
import net.minecraft.util.FastColor
import net.minecraft.world.item.DyeColor
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.VoxelShape
import org.joml.Matrix4f


abstract class RenderUtils {
    companion object {
        private val vectors: Array<Vector3> = Array(8) { Vector3() }

        fun renderBoundingBox(
            poseStack: PoseStack,
            renderBuffers: RenderBuffers,
            camera: Camera,
            projection: Matrix4f,
            bb: AABB,
            faces: Boolean,
            lineWidth: Float,
            lineDyeColor: DyeColor,
            faceDyeColor: DyeColor
        ) {
            val cameraPosition: Vec3 = camera.position
            val bufferSource: MultiBufferSource.BufferSource = renderBuffers.bufferSource()

            poseStack.pushPose()
            //Translate negative camera position
            poseStack.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z)

            RenderSystem.disableCull()
            RenderSystem.disableDepthTest()
            RenderSystem.enableBlend()
            RenderSystem.defaultBlendFunc()
            RenderSystem.setShader { GameRenderer.getPositionColorShader() }


            val lineColorR = FastColor.ARGB32.red(lineDyeColor.textureDiffuseColor) / 255.0F
            val lineColorG = FastColor.ARGB32.green(lineDyeColor.textureDiffuseColor) / 255.0F
            val lineColorB = FastColor.ARGB32.blue(lineDyeColor.textureDiffuseColor) / 255.0F
            val lineColorA = 0.95F

            val faceColorR = FastColor.ARGB32.red(faceDyeColor.textureDiffuseColor) / 255.0F
            val faceColorG = FastColor.ARGB32.green(faceDyeColor.textureDiffuseColor) / 255.0F
            val faceColorB = FastColor.ARGB32.blue(faceDyeColor.textureDiffuseColor) / 255.0F
            val faceColorA = 0.75F

            renderOutline(lineWidth, bufferSource, poseStack, bb, lineColorR, lineColorG, lineColorB, lineColorA)
            if (faces) {
                renderFaces(bufferSource, poseStack, bb, faceColorR, faceColorG, faceColorB, faceColorA)
            }
            RenderSystem.enableCull()
            RenderSystem.enableDepthTest()
            RenderSystem.disableBlend()
            poseStack.popPose()
        }

        fun renderFaces(
            bufferSource: MultiBufferSource.BufferSource,
            poseStack: PoseStack,
            bb: AABB,
            colorRed: Float,
            colorGreen: Float,
            colorBlue: Float,
            alpha: Float
        ) {
            val renderType: RenderType = LineRenderType.translucentLineRenderType(1f)
            val builder: VertexConsumer = bufferSource.getBuffer(renderType)
            val pose = poseStack.last()

            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, -1.0f, 0.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, -1.0f, 0.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, -1.0f, 0.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, -1.0f, 0.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 1.0f, 0.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 1.0f, 0.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 1.0f, 0.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 1.0f, 0.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, -1.0f, 0.0f, 0.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, -1.0f, 0.0f, 0.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, -1.0f, 0.0f, 0.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, -1.0f, 0.0f, 0.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 1.0f, 0.0f, 0.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 1.0f, 0.0f, 0.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 1.0f, 0.0f, 0.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 1.0f, 0.0f, 0.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, -1.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, -1.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, -1.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, -1.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, 1.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, 1.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, 1.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, 1.0f)

            bufferSource.endBatch(renderType)
        }

        fun renderOutline(
            lineWidth: Float,
            bufferSource: MultiBufferSource.BufferSource,
            poseStack: PoseStack,
            bb: AABB,
            colorRed: Float,
            colorGreen: Float,
            colorBlue: Float,
            alpha: Float
        ) {
            val renderType: RenderType = LineRenderType.lineRenderType(lineWidth)
            val builder: VertexConsumer = bufferSource.getBuffer(renderType)
            val pose = poseStack.last()
            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 1.0f, 0.0f, 0.0f)

            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 1.0f, 0.0f, 0.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 1.0f, 0.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 1.0f, 0.0f)

            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, 1.0f)
            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, 1.0f)
            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 1.0f, 0.0f)
            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 1.0f, 0.0f)
            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, -1.0f, 0.0f, 0.0f)
            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, -1.0f, 0.0f, 0.0f)
            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, 1.0f)
            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, 1.0f)
            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, -1.0f, 0.0f)
            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, -1.0f, 0.0f)
            builder.addVertex(pose, bb.minX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 1.0f, 0.0f, 0.0f)
            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 1.0f, 0.0f, 0.0f)
            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, -1.0f)
            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, -1.0f)
            builder.addVertex(pose, bb.minX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 1.0f, 0.0f, 0.0f)
            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 1.0f, 0.0f, 0.0f)
            builder.addVertex(pose, bb.maxX.toFloat(), bb.minY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 1.0f, 0.0f)
            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 1.0f, 0.0f)
            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.minZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, 1.0f)
            builder.addVertex(pose, bb.maxX.toFloat(), bb.maxY.toFloat(), bb.maxZ.toFloat())
                .setColor(colorRed, colorGreen, colorBlue, alpha)
                .setNormal(pose, 0.0f, 0.0f, 1.0f)
            bufferSource.endBatch(renderType)
        }


        /**
         * Builds a solid cuboid.
         * Expects VertexFormat of POSITION_COLOR.
         *
         * @param builder The [VertexConsumer]
         * @param c       The [Cuboid6]
         * @param r       Red color.
         * @param g       Green color.
         * @param b       Blue Color.
         * @param a       Alpha channel.
         */
        fun bufferCuboidSolid(
            builder: VertexConsumer,
            poseStack: PoseStack,
            c: Cuboid6,
            r: Float,
            g: Float,
            b: Float,
            a: Float
        ) {
            val pose = poseStack.last()

            builder.addVertex(pose, c.min.x.toFloat(), c.max.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.max.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.min.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.min.x.toFloat(), c.min.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.min.x.toFloat(), c.min.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.min.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.max.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.min.x.toFloat(), c.max.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.min.x.toFloat(), c.min.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.min.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.min.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.min.x.toFloat(), c.min.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.min.x.toFloat(), c.max.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.max.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.max.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.min.x.toFloat(), c.max.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.min.x.toFloat(), c.min.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.min.x.toFloat(), c.max.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.min.x.toFloat(), c.max.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.min.x.toFloat(), c.min.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.min.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.max.y.toFloat(), c.min.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.max.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
            builder.addVertex(pose, c.max.x.toFloat(), c.min.y.toFloat(), c.max.z.toFloat()).setColor(r, g, b, a)
        }

        fun bufferCuboidOutline(
            builder: VertexConsumer,
            poseStack: PoseStack,
            c: Cuboid6,
            r: Float,
            g: Float,
            b: Float,
            a: Float
        ) {
            bufferLinePair(builder, poseStack, c.min.x, c.min.y, c.min.z, c.max.x, c.min.y, c.min.z, r, g, b, a)
            bufferLinePair(builder, poseStack, c.max.x, c.min.y, c.min.z, c.max.x, c.min.y, c.max.z, r, g, b, a)
            bufferLinePair(builder, poseStack, c.max.x, c.min.y, c.max.z, c.min.x, c.min.y, c.max.z, r, g, b, a)
            bufferLinePair(builder, poseStack, c.min.x, c.min.y, c.max.z, c.min.x, c.min.y, c.min.z, r, g, b, a)
            bufferLinePair(builder, poseStack, c.min.x, c.max.y, c.min.z, c.max.x, c.max.y, c.min.z, r, g, b, a)
            bufferLinePair(builder, poseStack, c.max.x, c.max.y, c.min.z, c.max.x, c.max.y, c.max.z, r, g, b, a)
            bufferLinePair(builder, poseStack, c.max.x, c.max.y, c.max.z, c.min.x, c.max.y, c.max.z, r, g, b, a)
            bufferLinePair(builder, poseStack, c.min.x, c.max.y, c.max.z, c.min.x, c.max.y, c.min.z, r, g, b, a)
            bufferLinePair(builder, poseStack, c.min.x, c.min.y, c.min.z, c.min.x, c.max.y, c.min.z, r, g, b, a)
            bufferLinePair(builder, poseStack, c.max.x, c.min.y, c.min.z, c.max.x, c.max.y, c.min.z, r, g, b, a)
            bufferLinePair(builder, poseStack, c.max.x, c.min.y, c.max.z, c.max.x, c.max.y, c.max.z, r, g, b, a)
            bufferLinePair(builder, poseStack, c.min.x, c.min.y, c.max.z, c.min.x, c.max.y, c.max.z, r, g, b, a)
        }

        fun bufferShapeOutline(
            builder: VertexConsumer,
            poseStack: PoseStack?,
            shape: VoxelShape,
            r: Float,
            g: Float,
            b: Float,
            a: Float
        ) {
            shape.forAllEdges { x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double ->
                bufferLinePair(
                    builder, poseStack, x1, y1, z1, x2, y2, z2, r, g, b, a
                )
            }
        }

        private fun bufferLinePair(
            builder: VertexConsumer,
            poseStack: PoseStack?,
            x1: Double,
            y1: Double,
            z1: Double,
            x2: Double,
            y2: Double,
            z2: Double,
            r: Float,
            g: Float,
            b: Float,
            a: Float
        ) {
            val v1: Vector3 = vectors[0].set(x1, y1, z1).subtract(x2, y2, z2)
            val d = v1.mag()
            v1.divide(d)
            if (poseStack == null) {
                builder.addVertex(x1.toFloat(), y1.toFloat(), z1.toFloat())
                    .setColor(r, g, b, a)
                    .setNormal(v1.x.toFloat(), v1.y.toFloat(), v1.z.toFloat())
                    builder.addVertex(x2.toFloat(), y2.toFloat(), z2.toFloat())
                    .setColor(r, g, b, a)
                    .setNormal(v1.x.toFloat(), v1.y.toFloat(), v1.z.toFloat())
                } else {
                val pose = poseStack.last()
                builder.addVertex(pose, x1.toFloat(), y1.toFloat(), z1.toFloat())
                    .setColor(r, g, b, a)
                    .setNormal(v1.x.toFloat(), v1.y.toFloat(), v1.z.toFloat())
                    builder.addVertex(pose, x2.toFloat(), y2.toFloat(), z2.toFloat())
                    .setColor(r, g, b, a)
                    .setNormal(v1.x.toFloat(), v1.y.toFloat(), v1.z.toFloat())
                }
        }
    }
}