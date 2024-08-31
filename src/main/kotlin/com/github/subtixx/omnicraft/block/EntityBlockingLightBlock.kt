package com.github.subtixx.omnicraft.block

import com.github.subtixx.omnicraft.EntityBlockLightManager
import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.OmnicraftConfig
import com.github.subtixx.omnicraft.client.LineRenderType
import com.github.subtixx.omnicraft.light.LightTypes
import com.github.subtixx.omnicraft.utils.Cuboid6
import com.github.subtixx.omnicraft.utils.RenderUtils
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Camera
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderBuffers
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.level.BlockEvent
import org.joml.Matrix4f
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.plus
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3


class EntityBlockingLightBlock(
    properties: Properties, private val lightType: LightTypes
) : Block(properties) {
    public override fun getShape(
        state: BlockState, blockGetter: BlockGetter, pos: BlockPos, ctx: CollisionContext
    ): VoxelShape {
        return lightType.voxelShape
    }

    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, randomSource: RandomSource) {
        val particle = pos.toVec3().plus(lightType.flameOffset)

        level.addParticle(ParticleTypes.SMOKE, particle.x, particle.y, particle.z, 0.0, 0.0, 0.0)
        level.addParticle(ParticleTypes.FLAME, particle.x, particle.y, particle.z, 0.0, 0.0, 0.0)
    }

    public override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, moving: Boolean) {
        super.onPlace(state, level, pos, oldState, moving)

        if (!EntityBlockLightManager.getRegistryForLevel(level).isPresent) return
        val registry = EntityBlockLightManager.getRegistryForLevel(level).get()
        registry.registerLight(lightType.keyFactory.apply(pos), lightType.lightFactory.apply(pos))
    }

    override fun triggerEvent(state: BlockState, level: Level, pos: BlockPos, id: Int, param: Int): Boolean {
        if (!EntityBlockLightManager.getRegistryForLevel(level).isPresent) {
            return false
        }

        val registry = EntityBlockLightManager.getRegistryForLevel(level).get()
        if (registry.isRegistered(LightTypes.MegaTorch.name, pos)) {
            return false
        }
        return super.triggerEvent(state, level, pos, id, param)
    }

    public override fun onRemove(
        state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, moving: Boolean
    ) {
        EntityBlockLightManager.getRegistryForLevel(level)
            .ifPresent { reg: EntityBlockLightManager -> reg.unregisterLight(lightType.keyFactory.apply(pos)) }
        super.onRemove(state, level, pos, oldState, moving)
    }

    public override fun propagatesSkylightDown(state: BlockState, getter: BlockGetter, pos: BlockPos): Boolean {
        return true
    }

    fun onRenderWorldLast(
        player: Player,
        projectionMatrix: Matrix4f,
        poseStack: PoseStack,
        renderBuffers: RenderBuffers,
        camera: Camera
    ) {
        val bufferSource: MultiBufferSource.BufferSource = renderBuffers.bufferSource()
        var renderType: RenderType = LineRenderType.translucentLineRenderType(1.0F)
        var builder: VertexConsumer = bufferSource.getBuffer(renderType)

        poseStack.pushPose()

        val cameraPos = camera.position
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z)
        val colorR = 1.0f
        val colorG = 1.0f
        val colorB = 0.0f
        RenderUtils.bufferCuboidSolid(
            builder, poseStack, Cuboid6(
                -(OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble()),
                -(OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble()),
                -(OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble()),
                (OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble()),
                (OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble()),
                (OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble())
            ), colorR, colorG, colorB, 0.5f
        )
        bufferSource.endBatch(renderType)

        renderType = LineRenderType.lineRenderType(1.0F)
        builder = bufferSource.getBuffer(renderType)

        RenderUtils.bufferCuboidOutline(
            builder, poseStack, Cuboid6(
                -(OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble()),
                -(OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble()),
                -(OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble()),
                (OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble()),
                (OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble()),
                (OmnicraftConfig.SERVER.megaTorchRadius.asInt.toDouble())
            ), colorR, colorG, colorB, 0.75f
        )

        bufferSource.endBatch(renderType)
        poseStack.popPose()
    }

    companion object {
        @SubscribeEvent
        fun onBlockPlaced(event: BlockEvent.EntityPlaceEvent) {
            val placedBlock = event.placedBlock.block
            if (placedBlock !== ModBlocks.MEGA_TORCH) return
            val level = event.level as Level
            val pos = event.pos
            if (!EntityBlockLightManager.getRegistryForLevel(level).isPresent) {
                event.isCanceled = true
                return
            }
            val registry = EntityBlockLightManager.getRegistryForLevel(level).get()
            if (!registry.isRegistered(LightTypes.MegaTorch.name, pos)) return

            Omnicraft.LOGGER.warn("Light already registered at {}", pos)
            event.isCanceled = true
            // BUG: Item disappears from client-inventory????
            return
        }
    }
}