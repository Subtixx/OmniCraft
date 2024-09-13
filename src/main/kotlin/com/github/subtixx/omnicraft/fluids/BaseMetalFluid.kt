package com.github.subtixx.omnicraft.fluids

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.mod.ModFluids
import com.github.subtixx.omnicraft.resources.WorldResource
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.FluidTags
import net.minecraft.util.RandomSource
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.LavaFluid
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem

abstract class BaseMetalFluid(
    protected val block: DeferredBlock<LiquidBlock>,
    protected val fluidBucket: DeferredItem<BucketItem>,
    protected val fluidType: DeferredHolder<FluidType, FluidType>
) : LavaFluid() {
    override fun getBucket(): Item {
        return fluidBucket.get()
    }

    override fun getFluidType(): FluidType {
        return fluidType.get()
    }

    override fun getDripParticle(): ParticleOptions? {
        return null
    }

    @Deprecated("Deprecated in Java")
    override fun canConvertToSource(level: Level): Boolean {
        return false
    }

    override fun beforeDestroyingBlock(level: LevelAccessor, pos: BlockPos, state: BlockState) {
        val blockentity = if (state.hasBlockEntity()) level.getBlockEntity(pos) else null
        Block.dropResources(state, level, pos, blockentity)
    }

    override fun createLegacyBlock(state: FluidState): BlockState {
        return block.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state))
    }

    override fun isSame(fluid: Fluid): Boolean {
        return fluid === source || fluid === flowing
    }

    override fun canBeReplacedWith(
        fluidState: FluidState,
        blockReader: BlockGetter,
        pos: BlockPos,
        fluid: Fluid,
        direction: Direction
    ): Boolean {
        return direction == Direction.DOWN && !fluid.`is`(FluidTags.WATER)
    }

    override fun randomTick(level: Level, pos: BlockPos, state: FluidState, random: RandomSource) {
    }

    override fun animateTick(level: Level, pos: BlockPos, state: FluidState, random: RandomSource) {
    }

    class Flowing(
        block: DeferredBlock<LiquidBlock>,
        private val source: DeferredHolder<Fluid, FlowingFluid>,
        fluidBucket: DeferredItem<BucketItem>,
        fluidType: DeferredHolder<FluidType, FluidType>
    ) : BaseMetalFluid(
        block,
        fluidBucket,
        fluidType
    ) {
        override fun getFlowing(): Fluid {
            return this
        }

        override fun getSource(): Fluid {
            return source.get()
        }

        override fun createFluidStateDefinition(builder: StateDefinition.Builder<Fluid?, FluidState>) {
            super.createFluidStateDefinition(builder)
            builder.add(LEVEL)
        }

        override fun getAmount(state: FluidState): Int {
            return state.getValue(LEVEL)
        }

        override fun isSource(state: FluidState): Boolean {
            return false
        }
    }

    class Source(
        block: DeferredBlock<LiquidBlock>,
        private val flowing: DeferredHolder<Fluid, FlowingFluid>,
        fluidBucket: DeferredItem<BucketItem>,
        fluidType: DeferredHolder<FluidType, FluidType>
    ) : BaseMetalFluid(
        block,
        fluidBucket,
        fluidType
    ) {
        override fun getSource(): Fluid {
            return this
        }

        override fun getFlowing(): Fluid {
            return flowing.get()
        }

        override fun getAmount(state: FluidState): Int {
            return 8
        }

        override fun isSource(state: FluidState): Boolean {
            return true
        }
    }

    class Type(
        private val color: Int,
        private val stillTexture: ResourceLocation,
        private val flowingTexture: ResourceLocation
    ) : IClientFluidTypeExtensions {
        constructor(
            worldResource: WorldResource
        ) : this(
            0xa0a0b,
            ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "block/fluids/molten_metal"),
            ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "block/fluids/molten_metal")
        )

        override fun getTintColor(): Int {
            return color
        }

        override fun getStillTexture(): ResourceLocation {
            return stillTexture
        }

        override fun getFlowingTexture(): ResourceLocation {
            return flowingTexture
        }
    }
}