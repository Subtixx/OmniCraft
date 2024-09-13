package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.FlowingFluid
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks {
    val REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(Omnicraft.ID)

    fun createFluidBlock(name: String, fluid: FlowingFluid): DeferredBlock<Block> {
        return REGISTRY.register(name) { ->
            LiquidBlock(fluid, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER))
        }
    }
}
