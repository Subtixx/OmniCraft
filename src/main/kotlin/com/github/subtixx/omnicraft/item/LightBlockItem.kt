package com.github.subtixx.omnicraft.item

import com.github.subtixx.omnicraft.EntityBlockLightManager
import com.github.subtixx.omnicraft.OmnicraftConfig
import com.github.subtixx.omnicraft.block.EntityBlockingLightBlock
import com.github.subtixx.omnicraft.block.ModBlocks
import com.github.subtixx.omnicraft.light.LightTypes
import com.github.subtixx.omnicraft.utils.Vec3Extension
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Camera
import net.minecraft.client.renderer.RenderBuffers
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Block
import org.joml.Matrix4f

class LightBlockItem(block: Block, properties: Properties) : GenericBlockItem(block, properties) {
    private var focusedTorch: EntityBlockingLightBlock? = null
    private var focusedPos: BlockPos? = null

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        val pos = context.clickedPos
        val blockState = level.getBlockState(pos)
        val block = blockState.block
        if(!context.level.isClientSide) {
            return super.useOn(context)
        }

        if (block != ModBlocks.MEGA_TORCH) {
            focusedTorch = null
            focusedPos = context.clickedPos
            return super.useOn(context)
        }

        if (focusedPos != null && focusedTorch == null) {
            val torchBlock = level.getBlockState(focusedPos!!).block
            if (torchBlock != ModBlocks.MEGA_TORCH) {
                focusedTorch = null
                focusedPos = null
                return super.useOn(context)
            }
            focusedTorch = torchBlock as EntityBlockingLightBlock
        }

        if (focusedTorch != null) {
            if (focusedTorch == block) {
                focusedTorch = null
                focusedPos = null

                // Add chat message
                context.player?.displayClientMessage(
                    Component.literal("Cleared block at ${pos.x}, ${pos.y}, ${pos.z}"), false
                )

                return InteractionResult.SUCCESS
            }
            return super.useOn(context)
        }


        // Add chat message
        context.player?.displayClientMessage(
            Component.literal("Lighting block at ${pos.x}, ${pos.y}, ${pos.z}"), false
        )
        focusedTorch = block as EntityBlockingLightBlock
        focusedPos = pos
        return InteractionResult.SUCCESS
    }

    fun onRenderWorldLast(
        player: Player,
        projectionMatrix: Matrix4f,
        poseStack: PoseStack,
        renderBuffers: RenderBuffers,
        camera: Camera
    ) {
        if (focusedPos == null) return
        val block = player.level().getBlockState(focusedPos!!).block
        if (block != ModBlocks.MEGA_TORCH) {
            focusedTorch = null
            focusedPos = null
            return
        }

        if (focusedTorch == null) {
            focusedTorch = block as EntityBlockingLightBlock
        }

        if (!Vec3Extension.DistCubic(
                player.position(),
                focusedPos!!,
                OmnicraftConfig.SERVER.megaTorchRadius.asInt + 1
            )
        ) return

        focusedTorch?.onRenderWorldLast(player, projectionMatrix, poseStack, renderBuffers, camera)
    }
}