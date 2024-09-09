package com.github.subtixx.omnicraft.utils

import com.github.subtixx.omnicraft.utils.extensions.setColor
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.shapes.VoxelShape

@Suppress("unused")
object RenderUtils {
    /**
     * Renders a fully textured, solid cuboid described by the provided [AABB], usually obtained from [VoxelShape.bounds].
     * Texture widths (in pixels) are inferred to be 16 x the width of the quad, which matches normal block pixel texture sizes.
     */
    @JvmStatic
    fun renderTexturedCuboid(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        sprite: TextureAtlasSprite,
        packedLight: Int,
        packedOverlay: Int,
        bounds: AABB
    ) {
        renderTexturedCuboid(
            poseStack,
            buffer,
            sprite,
            packedLight,
            packedOverlay,
            bounds.minX.toFloat(),
            bounds.minY.toFloat(),
            bounds.minZ.toFloat(),
            bounds.maxX.toFloat(),
            bounds.maxY.toFloat(),
            bounds.maxZ.toFloat()
        )
    }

    /**
     * Renders a fully textured, solid cuboid described by the shape (minX, minY, minZ) x (maxX, maxY, maxZ).
     * Texture widths (in pixels) are inferred to be 16 x the width of the quad, which matches normal block pixel texture sizes.
     */
    @JvmStatic
    fun renderTexturedCuboid(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        sprite: TextureAtlasSprite,
        packedLight: Int,
        packedOverlay: Int,
        minX: Float,
        minY: Float,
        minZ: Float,
        maxX: Float,
        maxY: Float,
        maxZ: Float
    ) {
        renderTexturedCuboid(
            poseStack,
            buffer,
            sprite,
            packedLight,
            packedOverlay,
            minX,
            minY,
            minZ,
            maxX,
            maxY,
            maxZ,
            16f * (maxX - minX),
            16f * (maxY - minY),
            16f * (maxZ - minZ),
            true
        )
    }

    @JvmStatic
    fun renderTexturedCuboid(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        sprite: TextureAtlasSprite,
        packedLight: Int,
        packedOverlay: Int,
        minX: Float,
        minY: Float,
        minZ: Float,
        maxX: Float,
        maxY: Float,
        maxZ: Float,
        doShade: Boolean
    ) {
        renderTexturedCuboid(
            poseStack,
            buffer,
            sprite,
            packedLight,
            packedOverlay,
            minX,
            minY,
            minZ,
            maxX,
            maxY,
            maxZ,
            16f * (maxX - minX),
            16f * (maxY - minY),
            16f * (maxZ - minZ),
            doShade
        )
    }

    /**
     * Renders a fully textured, solid cuboid described by the shape (minX, minY, minZ) x (maxX, maxY, maxZ).
     * (xPixels, yPixels, zPixels) represent pixel widths for each side, which are used for texture (u, v) purposes.
     */
    @JvmStatic
    fun renderTexturedCuboid(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        sprite: TextureAtlasSprite,
        packedLight: Int,
        packedOverlay: Int,
        minX: Float,
        minY: Float,
        minZ: Float,
        maxX: Float,
        maxY: Float,
        maxZ: Float,
        xPixels: Float,
        yPixels: Float,
        zPixels: Float,
        doShade: Boolean
    ) {
        renderTexturedQuads(
            poseStack,
            buffer,
            sprite,
            packedLight,
            packedOverlay,
            getXVertices(minX, minY, minZ, maxX, maxY, maxZ),
            zPixels,
            yPixels,
            1f,
            0f,
            0f,
            doShade
        )
        renderTexturedQuads(
            poseStack,
            buffer,
            sprite,
            packedLight,
            packedOverlay,
            getYVertices(minX, minY, minZ, maxX, maxY, maxZ),
            zPixels,
            xPixels,
            0f,
            1f,
            0f,
            doShade
        )
        renderTexturedQuads(
            poseStack,
            buffer,
            sprite,
            packedLight,
            packedOverlay,
            getZVertices(minX, minY, minZ, maxX, maxY, maxZ),
            xPixels,
            yPixels,
            0f,
            0f,
            1f,
            doShade
        )
    }

    /**
     * <pre>
     * Q------Q.  ^ y
     * |`.    | `.|
     * |  `Q--+---Q--> x = maxY
     * |   |  |   |
     * P---+--P.  |
     * `. |    `.|
     * `P------P = minY
    </pre> *
     *
     * Renders a fully textured, solid trapezoidal cuboid described by the plane P, the plane Q, minY, and maxY.
     * (xPixels, yPixels, zPixels) represent pixel widths for each side, which are used for texture (u, v) purposes.
     */
    @JvmStatic
    fun renderTexturedTrapezoidalCuboid(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        sprite: TextureAtlasSprite,
        packedLight: Int,
        packedOverlay: Int,
        pMinX: Float,
        pMaxX: Float,
        pMinZ: Float,
        pMaxZ: Float,
        qMinX: Float,
        qMaxX: Float,
        qMinZ: Float,
        qMaxZ: Float,
        minY: Float,
        maxY: Float,
        xPixels: Float,
        yPixels: Float,
        zPixels: Float,
        invertNormal: Boolean
    ) {
        renderTexturedQuads(
            poseStack,
            buffer,
            sprite,
            packedLight,
            packedOverlay,
            getTrapezoidalCuboidXVertices(pMinX, pMaxX, pMinZ, pMaxZ, qMinX, qMaxX, qMinZ, qMaxZ, minY, maxY),
            zPixels,
            yPixels,
            (if (invertNormal) 0 else 1).toFloat(),
            0f,
            (if (invertNormal) 1 else 0).toFloat(),
            true
        )
        renderTexturedQuads(
            poseStack,
            buffer,
            sprite,
            packedLight,
            packedOverlay,
            getTrapezoidalCuboidYVertices(pMinX, pMaxX, pMinZ, pMaxZ, qMinX, qMaxX, qMinZ, qMaxZ, minY, maxY),
            zPixels,
            xPixels,
            0f,
            1f,
            0f,
            true
        )
        renderTexturedQuads(
            poseStack,
            buffer,
            sprite,
            packedLight,
            packedOverlay,
            getTrapezoidalCuboidZVertices(pMinX, pMaxX, pMinZ, pMaxZ, qMinX, qMaxX, qMinZ, qMaxZ, minY, maxY),
            xPixels,
            yPixels,
            (if (invertNormal) 1 else 0).toFloat(),
            0f,
            (if (invertNormal) 0 else 1).toFloat(),
            true
        )
    }

    /**
     * Renders a single textured quad, either by itself or as part of a larger cuboid construction.
     * `vertices` must be a set of vertices, usually obtained through [.getXVertices], [.getYVertices], or [.getZVertices]. Parameters are (x, y, z, u, v, normalSign) for each vertex.
     * (normalX, normalY, normalZ) are the normal vectors (positive), for the quad. For example, for an X quad, this will be (1, 0, 0).
     *
     * @param vertices The vertices.
     * @param uSize    The horizontal (u) texture size of the quad, in pixels.
     * @param vSize    The vertical (v) texture size of the quad, in pixels.
     */
    @JvmStatic
    fun renderTexturedQuads(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        sprite: TextureAtlasSprite,
        packedLight: Int,
        packedOverlay: Int,
        vertices: Array<FloatArray>,
        uSize: Float,
        vSize: Float,
        normalX: Float,
        normalY: Float,
        normalZ: Float,
        doShade: Boolean
    ) {
        for (v in vertices) {
            renderTexturedVertex(
                poseStack,
                buffer,
                packedLight,
                packedOverlay,
                v[0],
                v[1],
                v[2],
                sprite.getU(v[3] * uSize),
                sprite.getV(v[4] * vSize),
                v[5] * normalX,
                v[5] * normalY,
                v[5] * normalZ,
                doShade
            )
        }
    }

    @JvmStatic
    fun renderTexturedVertex(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        x: Float,
        y: Float,
        z: Float,
        u: Float,
        v: Float,
        normalX: Float,
        normalY: Float,
        normalZ: Float
    ) {
        renderTexturedVertex(
            poseStack,
            buffer,
            packedLight,
            packedOverlay,
            x,
            y,
            z,
            u,
            v,
            normalX,
            normalY,
            normalZ,
            true
        )
    }

    /**
     * Renders a single vertex as part of a quad.
     *
     *  * (x, y, z) describe the position of the vertex.
     *  * (u, v) describe the texture coordinates, typically will be a number of pixels (i.e. 16x something)
     *  * (normalX, normalY, normalZ) describe the normal vector to the quad.
     *
     */
    @JvmStatic
    fun renderTexturedVertex(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        packedLight: Int,
        packedOverlay: Int,
        x: Float,
        y: Float,
        z: Float,
        u: Float,
        v: Float,
        normalX: Float,
        normalY: Float,
        normalZ: Float,
        doShade: Boolean
    ) {
        val shade = if (doShade) getShade(normalX, normalY, normalZ) else 1f
        buffer.addVertex(poseStack.last().pose(), x, y, z)
            .setColor(shade, shade, shade, 1f)
            .setUv(u, v)
            .setLight(packedLight)
            .setOverlay(packedOverlay)
            .setNormal(poseStack.last(), normalX, normalY, normalZ)
    }

    /**
     * Converts a potentially angled normal into the 'nearest' directional step. Could potentially be reimplemented as an inverse lerp.
     */
    @JvmStatic
    fun getShade(normalX: Float, normalY: Float, normalZ: Float): Float {
        return getShadeForStep(Math.round(normalX), Math.round(normalY), Math.round(normalZ))
    }

    /**
     * Returns the static diffuse shade by MC for each directional face. The color value of a vertex should be multiplied by this.
     * Reimplements [net.minecraft.client.multiplayer.ClientLevel.getShade]
     */
    @JvmStatic
    fun getShadeForStep(normalX: Int, normalY: Int, normalZ: Int): Float {
        if (normalY == 1) return 1f
        if (normalY == -1) return 0.5f
        if (normalZ != 0) return 0.8f
        if (normalX != 0) return 0.6f
        return 1f
    }

    /**
     * <pre>
     * O------P.  ^ y
     * |`.    | `.|
     * |  `O--+---P--> x
     * |   |  |   |
     * O---+--P.  |
     * `. |    `.|
     * `O------P
    </pre> *
     *
     * @return A collection of vertices for two parallel faces of a cube, facing outwards, defined by (minX, minY, minZ) x (maxX, maxY, maxZ). Or the faces O and P in the above art
     */
    @JvmStatic
    fun getXVertices(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float): Array<FloatArray> {
        return arrayOf(
            floatArrayOf(minX, minY, minZ, 0f, 1f, 1f),  // +X
            floatArrayOf(minX, minY, maxZ, 1f, 1f, 1f),
            floatArrayOf(minX, maxY, maxZ, 1f, 0f, 1f),
            floatArrayOf(minX, maxY, minZ, 0f, 0f, 1f),

            floatArrayOf(maxX, minY, maxZ, 1f, 0f, -1f),  // -X
            floatArrayOf(maxX, minY, minZ, 0f, 0f, -1f),
            floatArrayOf(maxX, maxY, minZ, 0f, 1f, -1f),
            floatArrayOf(maxX, maxY, maxZ, 1f, 1f, -1f)
        )
    }

    /**
     * <pre>
     * O------O.  ^ y
     * |`.    | `.|
     * |  `O--+---O--> x
     * |   |  |   |
     * P---+--P.  |
     * `. |    `.|
     * `P------P
    </pre> *
     *
     * @return A collection of vertices for two parallel faces of a cube, facing outwards, defined by (minX, minY, minZ) x (maxX, maxY, maxZ). Or the faces O and P in the above art
     */
    @JvmStatic
    fun getYVertices(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float): Array<FloatArray> {
        return arrayOf(
            floatArrayOf(minX, maxY, minZ, 0f, 1f, 1f),  // +Y
            floatArrayOf(minX, maxY, maxZ, 1f, 1f, 1f),
            floatArrayOf(maxX, maxY, maxZ, 1f, 0f, 1f),
            floatArrayOf(maxX, maxY, minZ, 0f, 0f, 1f),

            floatArrayOf(minX, minY, maxZ, 1f, 0f, -1f),  // -Y
            floatArrayOf(minX, minY, minZ, 0f, 0f, -1f),
            floatArrayOf(maxX, minY, minZ, 0f, 1f, -1f),
            floatArrayOf(maxX, minY, maxZ, 1f, 1f, -1f)
        )
    }

    /**
     * <pre>
     * O------O.  ^ y
     * |`.    | `.|
     * |  `P--+---P--> x
     * |   |  |   |
     * O---+--O.  |
     * `. |    `.|
     * `P------P
    </pre> *
     *
     * @return A collection of vertices for two parallel faces of a cube, facing outwards, defined by (minX, minY, minZ) x (maxX, maxY, maxZ). Or the faces O and P in the above art
     */
    @JvmStatic
    fun getZVertices(minX: Float, minY: Float, minZ: Float, maxX: Float, maxY: Float, maxZ: Float): Array<FloatArray> {
        return arrayOf(
            floatArrayOf(maxX, minY, minZ, 0f, 1f, 1f),  // +Z
            floatArrayOf(minX, minY, minZ, 1f, 1f, 1f),
            floatArrayOf(minX, maxY, minZ, 1f, 0f, 1f),
            floatArrayOf(maxX, maxY, minZ, 0f, 0f, 1f),

            floatArrayOf(minX, minY, maxZ, 1f, 0f, -1f),  // -Z
            floatArrayOf(maxX, minY, maxZ, 0f, 0f, -1f),
            floatArrayOf(maxX, maxY, maxZ, 0f, 1f, -1f),
            floatArrayOf(minX, maxY, maxZ, 1f, 1f, -1f)
        )
    }

    /**
     * <pre>
     * P------P.  ^ y
     * |`.    | `.|
     * |  `+--+---+--> x
     * |   |  |   |
     * +---+--+.  |
     * `. |    `.|
     * `P------P
    </pre> *
     *
     * @return A collection of vertices for both sides of one of the diagonal faces of a cube defined by (minX, minY, minZ) x (maxX, maxY, maxZ). Or both sides of the face defined by vertices P in the above art.
     */
    @JvmStatic
    fun getDiagonalPlaneVertices(
        x1: Float,
        y1: Float,
        z1: Float,
        x2: Float,
        y2: Float,
        z2: Float,
        u1: Float,
        v1: Float,
        u2: Float,
        v2: Float
    ): Array<FloatArray> {
        return arrayOf(
            floatArrayOf(x1, y1, z1, u1, v1),
            floatArrayOf(x2, y1, z1, u2, v1),
            floatArrayOf(x2, y2, z2, u2, v2),
            floatArrayOf(x1, y2, z2, u1, v2),

            floatArrayOf(x2, y1, z1, u2, v1),
            floatArrayOf(x1, y1, z1, u1, v1),
            floatArrayOf(x1, y2, z2, u1, v2),
            floatArrayOf(x2, y2, z2, u2, v2)
        )
    }

    /**
     * <pre>
     * Q------Q.  ^ y
     * |`.    | `.|
     * |  `Q--+---Q--> x = maxY
     * |   |  |   |
     * P---+--P.  |
     * `. |    `.|
     * `P------P = minY
    </pre> *
     *
     * @return A collection of vertices for the positive and negative X outward faces of the above trapezoidal cuboid, defined by the plane P, and the plane Q, minY, and maxY.
     */
    @JvmStatic
    fun getTrapezoidalCuboidXVertices(
        pMinX: Float,
        pMaxX: Float,
        pMinZ: Float,
        pMaxZ: Float,
        qMinX: Float,
        qMaxX: Float,
        qMinZ: Float,
        qMaxZ: Float,
        minY: Float,
        maxY: Float
    ): Array<FloatArray> {
        return arrayOf(
            floatArrayOf(pMinX, minY, pMinZ, 0f, 1f, 1f),  // +X
            floatArrayOf(pMinX, minY, pMaxZ, 1f, 1f, 1f),
            floatArrayOf(qMinX, maxY, qMaxZ, 1f, 0f, 1f),
            floatArrayOf(qMinX, maxY, qMinZ, 0f, 0f, 1f),

            floatArrayOf(pMaxX, minY, pMaxZ, 1f, 0f, -1f),  // -X
            floatArrayOf(pMaxX, minY, pMinZ, 0f, 0f, -1f),
            floatArrayOf(qMaxX, maxY, qMinZ, 0f, 1f, -1f),
            floatArrayOf(qMaxX, maxY, qMaxZ, 1f, 1f, -1f),
        )
    }

    /**
     * <pre>
     * Q------Q.  ^ y
     * |`.    | `.|
     * |  `Q--+---Q--> x = maxY
     * |   |  |   |
     * P---+--P.  |
     * `. |    `.|
     * `P------P = minY
    </pre> *
     *
     * @return A collection of vertices for the positive and negative Y outward faces of the above trapezoidal cuboid, defined by the plane P, and the plane Q, minY, and maxY.
     */
    @JvmStatic
    fun getTrapezoidalCuboidYVertices(
        pMinX: Float,
        pMaxX: Float,
        pMinZ: Float,
        pMaxZ: Float,
        qMinX: Float,
        qMaxX: Float,
        qMinZ: Float,
        qMaxZ: Float,
        minY: Float,
        maxY: Float
    ): Array<FloatArray> {
        return arrayOf(
            floatArrayOf(qMinX, maxY, qMinZ, 0f, 1f, 1f),  // +Y
            floatArrayOf(qMinX, maxY, qMaxZ, 1f, 1f, 1f),
            floatArrayOf(qMaxX, maxY, qMaxZ, 1f, 0f, 1f),
            floatArrayOf(qMaxX, maxY, qMinZ, 0f, 0f, 1f),

            floatArrayOf(pMinX, minY, pMaxZ, 1f, 0f, -1f),  // -Y
            floatArrayOf(pMinX, minY, pMinZ, 0f, 0f, -1f),
            floatArrayOf(pMaxX, minY, pMinZ, 0f, 1f, -1f),
            floatArrayOf(pMaxX, minY, pMaxZ, 1f, 1f, -1f),
        )
    }

    /**
     * <pre>
     * Q------Q.  ^ y
     * |`.    | `.|
     * |  `Q--+---Q--> x = maxY
     * |   |  |   |
     * P---+--P.  |
     * `. |    `.|
     * `P------P = minY
    </pre> *
     *
     * @return A collection of vertices for the positive and negative X outward faces of the above trapezoidal cuboid, defined by the plane P, and the plane Q, minY, and maxY.
     */
    @JvmStatic
    fun getTrapezoidalCuboidZVertices(
        pMinX: Float,
        pMaxX: Float,
        pMinZ: Float,
        pMaxZ: Float,
        qMinX: Float,
        qMaxX: Float,
        qMinZ: Float,
        qMaxZ: Float,
        minY: Float,
        maxY: Float
    ): Array<FloatArray> {
        return arrayOf(
            floatArrayOf(pMaxX, minY, pMinZ, 0f, 1f, 1f),  // +Z
            floatArrayOf(pMinX, minY, pMinZ, 1f, 1f, 1f),
            floatArrayOf(qMinX, maxY, qMinZ, 1f, 0f, 1f),
            floatArrayOf(qMaxX, maxY, qMinZ, 0f, 0f, 1f),

            floatArrayOf(pMinX, minY, pMaxZ, 1f, 0f, -1f),  // -Z
            floatArrayOf(pMaxX, minY, pMaxZ, 0f, 0f, -1f),
            floatArrayOf(qMaxX, maxY, qMaxZ, 0f, 1f, -1f),
            floatArrayOf(qMinX, maxY, qMaxZ, 1f, 1f, -1f)
        )
    }

    @JvmStatic
    fun setShaderColor(packedColor: Int) {
        val color = Color(packedColor)

        RenderSystem.setShaderColor(color.red, color.green, color.blue, color.alpha)
    }

    @JvmStatic
    fun setShaderColor(graphics: GuiGraphics, packedColor: Int) {
        graphics.setColor(Color(packedColor))
    }
}