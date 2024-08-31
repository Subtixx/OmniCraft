package com.github.subtixx.omnicraft.client

import com.github.subtixx.omnicraft.block.EntityBlockingLightBlock
import com.github.subtixx.omnicraft.item.LightBlockItem
import com.github.subtixx.omnicraft.block.ModBlocks
import com.github.subtixx.omnicraft.item.ModItems
import com.github.subtixx.omnicraft.item.TapeItem
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderBuffers
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import org.joml.Matrix4f

object ClientClass {
    fun onLogIn() {
        BoxHandler.clear()
    }

    fun onLogOut() {
        BoxHandler.clear()
    }

    fun onPlayerTick(player: Player) {
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
        player: Player?,
        projectionMatrix: Matrix4f,
        poseStack: PoseStack,
        renderBuffers: RenderBuffers,
        camera: Camera
    ) {
        if (player == null) return
        if (player.isHolding(ModItems.TAPE_MEASURE_ITEM)) {
            TapeItem.onRenderWorldLast(player, projectionMatrix, poseStack, renderBuffers, camera)
        } else if (player.getItemInHand(InteractionHand.MAIN_HAND).item == ModItems.MEGA_TORCH_ITEM) {
            val holdingItem = player.mainHandItem.item as LightBlockItem
            holdingItem.onRenderWorldLast(player, projectionMatrix, poseStack, renderBuffers, camera)
        } else {
            // Check block hitz
            val rayHit: HitResult = Minecraft.getInstance().hitResult ?: return
            if (rayHit.type != HitResult.Type.BLOCK) return

            val blockHitResult: BlockHitResult = rayHit as BlockHitResult
            val block = player.level().getBlockState(blockHitResult.blockPos).block
            if (block !is EntityBlockingLightBlock) return

            block.onRenderWorldLast(player, projectionMatrix, poseStack, renderBuffers, camera)
        }
    }

    fun onRenderGui(
        player: Player?, guiGraphics: GuiGraphics
    ) {
        if (player == null || !player.isHolding(ModItems.TAPE_MEASURE_ITEM)) return

        val boxList = BoxHandler.getBoxList()

        if (boxList.isEmpty()) return

        val hit = Minecraft.getInstance().hitResult
        if (hit == null || hit.type != HitResult.Type.BLOCK) return
        val currentDimension: ResourceKey<Level?> = player.level().dimension()
        val blockHitResult: BlockHitResult = hit as BlockHitResult
        for (box in boxList) {
            if (box.dimension.location() != currentDimension.location()) continue
            if (!box.contains(blockHitResult.blockPos.center)) continue
            box.renderGui(guiGraphics, DyeColor.ORANGE.textColor)
            break
        }
    }
}
