package com.github.subtixx.omnicraft.client

import com.github.subtixx.omnicraft.utils.Platform
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderBuffers
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.culling.Frustum
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.joml.Matrix4f
import kotlin.math.max
import kotlin.math.min


// Credit: MadeBaruna - https://github.com/MadeBaruna/BlockMeter/blob/master/src/main/java/win/baruna/blockmeter/MeasureBox.java
class MeasurementBox internal constructor(
    block: BlockPos, dimensionKey: ResourceKey<Level?>,
    private var lineColor: DyeColor,
    private var boxColor: DyeColor? = null,
    private var textWidthColor: DyeColor? = null,
    private var textDepthColor: DyeColor? = null,
    private var textHeightColor: DyeColor? = null,
    private var textDiagonalColor: DyeColor? = null
) {
    val startPos: BlockPos = block
    var endPos: BlockPos
    var box: AABB? = null
    private val dimensionKey: ResourceKey<Level?>
    var isFinished: Boolean
        private set

    val center: Vec3
        get() {
            if (box == null) return Vec3(0.0, 0.0, 0.0)

            return Vec3(
                (box!!.minX + box!!.maxX) / 2.0,
                (box!!.minY + box!!.maxY) / 2.0,
                (box!!.minZ + box!!.maxZ) / 2.0
            )
        }
    val dimension: ResourceKey<Level?> get() = dimensionKey

    init {
        this.endPos = block
        this.dimensionKey = dimensionKey
        this.isFinished = false

        if (boxColor === null) {
            this.boxColor = this.lineColor
        }

        if (textWidthColor === null) {
            this.textWidthColor = this.lineColor
        }

        if (textDepthColor === null) {
            this.textDepthColor = this.lineColor
        }

        if (textHeightColor === null) {
            this.textHeightColor = this.lineColor
        }

        if (textDiagonalColor === null) {
            this.textDiagonalColor = this.lineColor
        }

        /*val textColor: TextColor = Platform.getTextColor()
        if (textColor === TextColor.XYZRGB) {
            this.textX = Platform.getTextColor().getColor(BoxHandler.random, Direction.Axis.X)
            this.textY = Platform.getTextColor().getColor(BoxHandler.random, Direction.Axis.Y)
            this.textZ = Platform.getTextColor().getColor(BoxHandler.random, Direction.Axis.Z)
        } else {
            val color: DyeColor? = textColor.getColor(BoxHandler.random, null)
            this.textX = color
            this.textY = color
            this.textZ = color
        }

        this.textDiagonal = Platform.getTextColor().getColor(BoxHandler.random, null)*/

        this.setBoundingBox()
    }

    private fun setBoundingBox() {
        val ax: Int = startPos.x
        val ay: Int = startPos.y
        val az: Int = startPos.z
        val bx: Int = endPos.x
        val by: Int = endPos.y
        val bz: Int = endPos.z

        this.box = AABB(
            min(ax.toDouble(), bx.toDouble()),
            min(ay.toDouble(), by.toDouble()),
            min(az.toDouble(), bz.toDouble()),
            (max(ax.toDouble(), bx.toDouble()) + 1),
            (max(ay.toDouble(), by.toDouble()) + 1),
            (max(az.toDouble(), bz.toDouble()) + 1)
        )
    }

    fun setBlockEnd(blockEnd: BlockPos) {
        this.endPos = blockEnd
        this.setBoundingBox()
    }

    fun render(
        poseStack: PoseStack,
        renderBuffers: RenderBuffers,
        camera: Camera,
        projection: Matrix4f
    ) {
        if (box == null) return

        val color: FloatArray = lineColor.textureDiffuseColors
        val r = color[0]
        val g = color[1]
        val b = color[2]
        val a = 0.95f

        val pos: Vec3 = camera.position

        val distance: Double = box!!.center.distanceTo(pos)
        var lineWidth: Float = Platform.getLineWidth()
        if (distance > 48) {
            lineWidth = Platform.getLineWidthMax()
        }

        val bufferSource: MultiBufferSource.BufferSource = renderBuffers.bufferSource()

        poseStack.pushPose()
        val renderType: RenderType = LineRenderType.lineRenderType(lineWidth)
        val builder: VertexConsumer = bufferSource.getBuffer(renderType)
        //Translate negative camera position
        poseStack.translate(-pos.x, -pos.y, -pos.z)
        //Render the outline
        //LevelRenderer.renderLineBox(poseStack, builder, box!!, r, g, b, a)

        val f: Float = box!!.minX.toFloat()
        val f1: Float = box!!.minY.toFloat()
        val f2: Float = box!!.minZ.toFloat()
        val f3: Float = box!!.maxX.toFloat()
        val f4: Float = box!!.maxY.toFloat()
        val f5: Float = box!!.maxZ.toFloat()
        val red: Float = r
        val green: Float = g
        val blue: Float = b
        val alpha: Float = a
        val red2: Float = r
        val green2: Float = g
        val blue2: Float = b

        val pose = poseStack.last()

        // Disable depth + cull
        RenderSystem.disableCull()
        RenderSystem.disableDepthTest()
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }

        builder.vertex(pose, f, f1, f2).color(red, green2, blue2, alpha).normal(pose, 1.0f, 0.0f, 0.0f).endVertex()
        builder.vertex(pose, f3, f1, f2).color(red, green2, blue2, alpha).normal(pose, 1.0f, 0.0f, 0.0f).endVertex()
        builder.vertex(pose, f, f1, f2).color(red2, green, blue2, alpha).normal(pose, 0.0f, 1.0f, 0.0f).endVertex()
        builder.vertex(pose, f, f4, f2).color(red2, green, blue2, alpha).normal(pose, 0.0f, 1.0f, 0.0f).endVertex()
        builder.vertex(pose, f, f1, f2).color(red2, green2, blue, alpha).normal(pose, 0.0f, 0.0f, 1.0f).endVertex()
        builder.vertex(pose, f, f1, f5).color(red2, green2, blue, alpha).normal(pose, 0.0f, 0.0f, 1.0f).endVertex()
        builder.vertex(pose, f3, f1, f2).color(red, green, blue, alpha).normal(pose, 0.0f, 1.0f, 0.0f).endVertex()
        builder.vertex(pose, f3, f4, f2).color(red, green, blue, alpha).normal(pose, 0.0f, 1.0f, 0.0f).endVertex()
        builder.vertex(pose, f3, f4, f2).color(red, green, blue, alpha).normal(pose, -1.0f, 0.0f, 0.0f).endVertex()
        builder.vertex(pose, f, f4, f2).color(red, green, blue, alpha).normal(pose, -1.0f, 0.0f, 0.0f).endVertex()
        builder.vertex(pose, f, f4, f2).color(red, green, blue, alpha).normal(pose, 0.0f, 0.0f, 1.0f).endVertex()
        builder.vertex(pose, f, f4, f5).color(red, green, blue, alpha).normal(pose, 0.0f, 0.0f, 1.0f).endVertex()
        builder.vertex(pose, f, f4, f5).color(red, green, blue, alpha).normal(pose, 0.0f, -1.0f, 0.0f).endVertex()
        builder.vertex(pose, f, f1, f5).color(red, green, blue, alpha).normal(pose, 0.0f, -1.0f, 0.0f).endVertex()
        builder.vertex(pose, f, f1, f5).color(red, green, blue, alpha).normal(pose, 1.0f, 0.0f, 0.0f).endVertex()
        builder.vertex(pose, f3, f1, f5).color(red, green, blue, alpha).normal(pose, 1.0f, 0.0f, 0.0f).endVertex()
        builder.vertex(pose, f3, f1, f5).color(red, green, blue, alpha).normal(pose, 0.0f, 0.0f, -1.0f).endVertex()
        builder.vertex(pose, f3, f1, f2).color(red, green, blue, alpha).normal(pose, 0.0f, 0.0f, -1.0f).endVertex()
        builder.vertex(pose, f, f4, f5).color(red, green, blue, alpha).normal(pose, 1.0f, 0.0f, 0.0f).endVertex()
        builder.vertex(pose, f3, f4, f5).color(red, green, blue, alpha).normal(pose, 1.0f, 0.0f, 0.0f).endVertex()
        builder.vertex(pose, f3, f1, f5).color(red, green, blue, alpha).normal(pose, 0.0f, 1.0f, 0.0f).endVertex()
        builder.vertex(pose, f3, f4, f5).color(red, green, blue, alpha).normal(pose, 0.0f, 1.0f, 0.0f).endVertex()
        builder.vertex(pose, f3, f4, f2).color(red, green, blue, alpha).normal(pose, 0.0f, 0.0f, 1.0f).endVertex()
        builder.vertex(pose, f3, f4, f5).color(red, green, blue, alpha).normal(pose, 0.0f, 0.0f, 1.0f).endVertex()


        bufferSource.endBatch(renderType)

        val renderType2: RenderType = LineRenderType.translucentLineRenderType(lineWidth)
        val builder2: VertexConsumer = bufferSource.getBuffer(renderType2)

        // Render all 6 faces as quads
        builder2.vertex(pose, f, f1, f2).color(r, g, b, a / 2).normal(pose, 0.0f, -1.0f, 0.0f).endVertex()
        builder2.vertex(pose, f3, f1, f2).color(r, g, b, a / 2).normal(pose, 0.0f, -1.0f, 0.0f).endVertex()
        builder2.vertex(pose, f3, f1, f5).color(r, g, b, a / 2).normal(pose, 0.0f, -1.0f, 0.0f).endVertex()
        builder2.vertex(pose, f, f1, f5).color(r, g, b, a / 2).normal(pose, 0.0f, -1.0f, 0.0f).endVertex()

        builder2.vertex(pose, f, f4, f2).color(r, g, b, a / 2).normal(pose, 0.0f, 1.0f, 0.0f).endVertex()
        builder2.vertex(pose, f3, f4, f2).color(r, g, b, a / 2).normal(pose, 0.0f, 1.0f, 0.0f).endVertex()
        builder2.vertex(pose, f3, f4, f5).color(r, g, b, a / 2).normal(pose, 0.0f, 1.0f, 0.0f).endVertex()
        builder2.vertex(pose, f, f4, f5).color(r, g, b, a / 2).normal(pose, 0.0f, 1.0f, 0.0f).endVertex()

        builder2.vertex(pose, f, f1, f2).color(r, g, b, a / 2).normal(pose, -1.0f, 0.0f, 0.0f).endVertex()
        builder2.vertex(pose, f, f4, f2).color(r, g, b, a / 2).normal(pose, -1.0f, 0.0f, 0.0f).endVertex()
        builder2.vertex(pose, f, f4, f5).color(r, g, b, a / 2).normal(pose, -1.0f, 0.0f, 0.0f).endVertex()
        builder2.vertex(pose, f, f1, f5).color(r, g, b, a / 2).normal(pose, -1.0f, 0.0f, 0.0f).endVertex()

        builder2.vertex(pose, f3, f1, f2).color(r, g, b, a / 2).normal(pose, 1.0f, 0.0f, 0.0f).endVertex()
        builder2.vertex(pose, f3, f4, f2).color(r, g, b, a / 2).normal(pose, 1.0f, 0.0f, 0.0f).endVertex()
        builder2.vertex(pose, f3, f4, f5).color(r, g, b, a / 2).normal(pose, 1.0f, 0.0f, 0.0f).endVertex()
        builder2.vertex(pose, f3, f1, f5).color(r, g, b, a / 2).normal(pose, 1.0f, 0.0f, 0.0f).endVertex()

        builder2.vertex(pose, f, f1, f2).color(r, g, b, a / 2).normal(pose, 0.0f, 0.0f, -1.0f).endVertex()
        builder2.vertex(pose, f3, f1, f2).color(r, g, b, a / 2).normal(pose, 0.0f, 0.0f, -1.0f).endVertex()
        builder2.vertex(pose, f3, f4, f2).color(r, g, b, a / 2).normal(pose, 0.0f, 0.0f, -1.0f).endVertex()
        builder2.vertex(pose, f, f4, f2).color(r, g, b, a / 2).normal(pose, 0.0f, 0.0f, -1.0f).endVertex()

        builder2.vertex(pose, f, f1, f5).color(r, g, b, a / 2).normal(pose, 0.0f, 0.0f, 1.0f).endVertex()
        builder2.vertex(pose, f3, f1, f5).color(r, g, b, a / 2).normal(pose, 0.0f, 0.0f, 1.0f).endVertex()
        builder2.vertex(pose, f3, f4, f5).color(r, g, b, a / 2).normal(pose, 0.0f, 0.0f, 1.0f).endVertex()
        builder2.vertex(pose, f, f4, f5).color(r, g, b, a / 2).normal(pose, 0.0f, 0.0f, 1.0f).endVertex()

        bufferSource.endBatch(renderType2)

        poseStack.popPose()

        //Render the line length text
        drawLength(poseStack, camera, projection, bufferSource)
    }

    fun renderGui(guiGraphics: net.minecraft.client.gui.GuiGraphics, textColor: Int) {
        if (box == null) return

        val lengthX: Int = box!!.xsize.toInt()
        val lengthY: Int = box!!.ysize.toInt()
        val lengthZ: Int = box!!.zsize.toInt()

        val boxCenter: Vec3 = box!!.center
        val diagonalLength: Double =
            Vec3(box!!.minX, box!!.minY, box!!.minZ).distanceTo(Vec3(box!!.maxX, box!!.maxY, box!!.maxZ))

        val font: Font = Minecraft.getInstance().font
        val fontHeight = font.lineHeight
        val windowWidth: Int = Minecraft.getInstance().window.guiScaledWidth
        val windowHeight: Int = Minecraft.getInstance().window.guiScaledHeight

        var nextY = windowHeight - fontHeight - 10

        guiGraphics.drawString(
            font, String.format("Diagonal: %.2f", diagonalLength), 10, nextY, textColor
        )
        nextY -= fontHeight

        guiGraphics.drawString(
            font, String.format("Depth: %d", lengthZ), 10, nextY, textColor
        )
        nextY -= fontHeight

        guiGraphics.drawString(
            font, String.format("Height: %d", lengthY), 10, nextY, textColor
        )
        nextY -= fontHeight

        guiGraphics.drawString(
            font, String.format("Width: %d", lengthX), 10, nextY, textColor
        )
    }

    private fun drawLength(
        poseStack: PoseStack, camera: Camera, projection: Matrix4f, bufferSource: MultiBufferSource.BufferSource
    ) {
        if (box == null) return

        val lengthX: Int = box!!.xsize.toInt()
        val lengthY: Int = box!!.ysize.toInt()
        val lengthZ: Int = box!!.zsize.toInt()
        val boxCenter: Vec3 = box!!.center
        val diagonalLength: Double =
            Vec3(box!!.minX, box!!.minY, box!!.minZ).distanceTo(Vec3(box!!.maxX, box!!.maxY, box!!.maxZ))

        val pos: Vec3 = camera.position

        val clippingHelper = Frustum(poseStack.last().pose(), projection)
        clippingHelper.prepare(pos.x, pos.y, pos.z)

        val boxT: AABB = box!!.inflate(0.08)

        val lines: MutableList<Line> = ArrayList()
        lines.add(
            Line.createLine(
                AABB(boxT.minX, boxT.minY, boxT.minZ, boxT.minX, boxT.minY, boxT.maxZ), pos, clippingHelper
            )
        )
        lines.add(
            Line.createLine(
                AABB(boxT.minX, boxT.maxY, boxT.minZ, boxT.minX, boxT.maxY, boxT.maxZ), pos, clippingHelper
            )
        )
        lines.add(
            Line.createLine(
                AABB(boxT.maxX, boxT.minY, boxT.minZ, boxT.maxX, boxT.minY, boxT.maxZ), pos, clippingHelper
            )
        )
        lines.add(
            Line.createLine(
                AABB(boxT.maxX, boxT.maxY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos, clippingHelper
            )
        )
        lines.sort()
        val lineZ: Vec3 = lines[0].line.center

        lines.clear()
        lines.add(
            Line.createLine(
                AABB(boxT.minX, boxT.minY, boxT.minZ, boxT.minX, boxT.maxY, boxT.minZ), pos, clippingHelper
            )
        )
        lines.add(
            Line.createLine(
                AABB(boxT.minX, boxT.minY, boxT.maxZ, boxT.minX, boxT.maxY, boxT.maxZ), pos, clippingHelper
            )
        )
        lines.add(
            Line.createLine(
                AABB(boxT.maxX, boxT.minY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.minZ), pos, clippingHelper
            )
        )
        lines.add(
            Line.createLine(
                AABB(boxT.maxX, boxT.minY, boxT.maxZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos, clippingHelper
            )
        )
        lines.sort()

        val lineY: Vec3 = lines[0].line.center
        lines.clear()
        lines.add(
            Line.createLine(
                AABB(boxT.minX, boxT.minY, boxT.minZ, boxT.maxX, boxT.minY, boxT.minZ), pos, clippingHelper
            )
        )
        lines.add(
            Line.createLine(
                AABB(boxT.minX, boxT.minY, boxT.maxZ, boxT.maxX, boxT.minY, boxT.maxZ), pos, clippingHelper
            )
        )
        lines.add(
            Line.createLine(
                AABB(boxT.minX, boxT.maxY, boxT.minZ, boxT.maxX, boxT.maxY, boxT.minZ), pos, clippingHelper
            )
        )
        lines.add(
            Line.createLine(
                AABB(boxT.minX, boxT.maxY, boxT.maxZ, boxT.maxX, boxT.maxY, boxT.maxZ), pos, clippingHelper
            )
        )
        lines.sort()

        val lineX: Vec3 = lines[0].line.center
        lines.clear()

        poseStack.pushPose()
        poseStack.translate(-pos.x, -pos.y, -pos.z)

        drawText(
            poseStack,
            camera,
            boxCenter,
            Component.literal(String.format("%.2f", diagonalLength)),
            DyeColor.BLACK,
            bufferSource
        );

        drawText(
            poseStack,
            camera,
            Vec3(lineX.x, lineX.y, lineX.z),
            Component.literal(lengthX.toString()),
            DyeColor.BLACK,
            bufferSource
        )
        drawText(
            poseStack,
            camera,
            Vec3(lineY.x, lineY.y, lineY.z),
            Component.literal(lengthY.toString()),
            DyeColor.BLACK,
            bufferSource
        )
        drawText(
            poseStack,
            camera,
            Vec3(lineZ.x, lineZ.y, lineZ.z),
            Component.literal(lengthZ.toString()),
            DyeColor.BLACK,
            bufferSource
        )

        poseStack.popPose()
    }

    private fun drawText(
        poseStack: PoseStack,
        camera: Camera,
        pos: Vec3,
        length: Component,
        textColor: DyeColor,
        bufferSource: MultiBufferSource.BufferSource
    ) {
        val font: Font = Minecraft.getInstance().font
        val size: Float = Platform.getTextSize()

        poseStack.pushPose()
        poseStack.translate(pos.x, pos.y + size * 5.0, pos.z)
        poseStack.mulPose(camera.rotation())
        poseStack.scale(-size, -size, -size)
        poseStack.translate(-font.width(length) / 2f, 0f, 0f)
        val pose: Matrix4f = poseStack.last().pose()
        font.drawInBatch(
            length, 0f, 0f, textColor.textColor, false, pose, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880
        )
        poseStack.popPose()
    }

    fun setFinished() {
        this.isFinished = true
    }

    fun contains(pos: Vec3): Boolean {
        return box!!.contains(pos)
    }

    private class Line(val line: AABB, val isVisible: Boolean, val distance: Double) : Comparable<Line> {
        override fun compareTo(other: Line): Int {
            return if (isVisible) {
                if (other.isVisible) distance.compareTo(other.distance) else -1
            } else {
                if (other.isVisible) 1 else 0
            }
        }

        companion object {
            fun createLine(line: AABB, pos: Vec3, clippingHelper: Frustum): Line {
                return Line(line, clippingHelper.isVisible(line), line.center.distanceTo(pos))
            }
        }
    }
}
