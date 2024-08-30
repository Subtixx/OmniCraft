package com.github.subtixx.omnicraft.client

import com.github.subtixx.omnicraft.item.ModItems
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderBuffers
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import org.joml.Matrix4f
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import java.util.function.Consumer

object ClientClass {
    fun onLogIn() {
        BoxHandler.clear()
    }

    fun onLogOut() {
        BoxHandler.clear()
    }

    fun onPlayerTick(player: net.minecraft.world.entity.player.Player) {
        if (Minecraft.getInstance().player !== player) return

        if (!player.isHolding(ModItems.TAPE_MEASURE_ITEM)) {
            //BoxHandler.clear()
            return
        }

        val boxList = BoxHandler.getBoxList()
        if (boxList.isEmpty()) return

        val lastBox = boxList[boxList.size - 1]
        if (lastBox.isFinished) return

        val rayHit: HitResult? = Minecraft.getInstance().hitResult

        if (rayHit != null && rayHit.type == HitResult.Type.BLOCK) {
            val blockHitResult: BlockHitResult = rayHit as BlockHitResult
            lastBox.setBlockEnd(BlockPos(blockHitResult.blockPos))
        }
    }

    fun onRenderWorldLast(
        player: net.minecraft.world.entity.player.Player?,
        projectionMatrix: Matrix4f,
        poseStack: PoseStack,
        renderBuffers: RenderBuffers,
        camera: Camera
    ) {
        if (player == null || !player.isHolding(ModItems.TAPE_MEASURE_ITEM)) return

        val currentDimension: ResourceKey<Level?> = player.level().dimension()
        poseStack.pushPose()
        val boxList = BoxHandler.getBoxList()
        boxList.forEach(Consumer { box: MeasurementBox? ->
            if (box == null) return@Consumer
            // Skip boxes that are too far away
            if(box.center.distanceTo(player.blockPosition().toVec3()) > 100) return@Consumer

            // Skip boxes that are not in the current dimension
            if(box.dimension.location() != currentDimension.location()) return@Consumer

            box.render(poseStack, renderBuffers, camera, projectionMatrix)
        })
        poseStack.popPose()
    }

    fun onRenderGui(
        player: net.minecraft.world.entity.player.Player?, guiGraphics: net.minecraft.client.gui.GuiGraphics
    ) {
        if (player == null || !player.isHolding(ModItems.TAPE_MEASURE_ITEM)) return

        val boxList = BoxHandler.getBoxList()

        if (boxList.isEmpty()) return

        val hit = Minecraft.getInstance().hitResult
        if (hit == null || hit.type != HitResult.Type.BLOCK) return
        val currentDimension: ResourceKey<Level?> = player.level().dimension()
        val blockHitResult: BlockHitResult = hit as BlockHitResult
        for (box in boxList) {
            if(box.dimension.location() != currentDimension.location()) continue
            if (!box.contains(blockHitResult.blockPos.center)) continue
            box.renderGui(guiGraphics, DyeColor.ORANGE.textColor)
            break
        }
    }
}
