package com.github.subtixx.omnicraft.item

import com.github.subtixx.omnicraft.client.BoxHandler
import com.github.subtixx.omnicraft.client.MeasurementBox
import com.github.subtixx.omnicraft.utils.Platform
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Camera
import net.minecraft.client.renderer.RenderBuffers
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import org.joml.Matrix4f
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import java.util.function.Consumer

class TapeItem(properties: Properties) : net.minecraft.world.item.Item(properties) {
    //BoxHandler.clear() -- On left click
    override fun useOn(context: UseOnContext): InteractionResult {
        val player: Player = context.player ?: return super.useOn(context)

        if (Platform.isValidUser(player)) {
            return BoxHandler.addBox(player, context.clickedPos)
        }

        return super.useOn(context)
    }


    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (!Platform.isValidUser(player)) {
            return super.use(level, player, usedHand)
        }

        if (!player.isShiftKeyDown) {
            // TODO: Right click in air -> open gui
            return super.use(level, player, usedHand)
        }

        BoxHandler.undo()
        return super.use(level, player, usedHand)
    }

    companion object {
        fun onRenderWorldLast(
            player: Player,
            projectionMatrix: Matrix4f,
            poseStack: PoseStack,
            renderBuffers: RenderBuffers,
            camera: Camera
        ) {
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
    }
}
