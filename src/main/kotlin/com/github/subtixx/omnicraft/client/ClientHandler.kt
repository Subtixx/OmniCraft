package com.github.subtixx.omnicraft.client

import com.github.subtixx.omnicraft.client.ClientClass.onLogIn
import com.github.subtixx.omnicraft.client.ClientClass.onLogOut
import com.github.subtixx.omnicraft.client.ClientClass.onPlayerTick
import com.github.subtixx.omnicraft.client.ClientClass.onRenderWorldLast
import com.github.subtixx.omnicraft.client.ClientClass.onRenderGui
import net.minecraft.client.Minecraft
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingIn
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingOut
import net.neoforged.neoforge.client.event.RenderGuiEvent
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent

class ClientHandler {
    companion object {
        @SubscribeEvent
        fun onLogOut(event: LoggingOut?) {
            onLogOut()
        }

        @SubscribeEvent
        fun onLogIn(event: LoggingIn?) {
            onLogIn()
        }

        @SubscribeEvent
        fun onRenderGui(event: RenderGuiEvent.Post) {
            val minecraft = Minecraft.getInstance()
            val player = minecraft.player

            onRenderGui(player, event.guiGraphics)
        }

        @SubscribeEvent
        fun onRenderWorldLast(event: RenderLevelStageEvent) {
            if (event.stage !== RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return

            val minecraft = Minecraft.getInstance()
            val player = minecraft.player
            val projectionMatrix = event.projectionMatrix
            val poseStack = event.poseStack
            val renderBuffers = minecraft.renderBuffers()
            val camera = minecraft.gameRenderer.mainCamera

            onRenderWorldLast(player, projectionMatrix, poseStack, renderBuffers, camera)
        }

        @SubscribeEvent
        fun onPlayerTick(event: PlayerTickEvent.Post) {
            if (!event.entity.level().isClientSide) return

            onPlayerTick(event.entity)
        }
    }
}