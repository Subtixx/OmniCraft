package com.github.subtixx.omnicraft.block

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.light.LightTypes
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import java.util.function.Supplier

object ModBlocks {
    val REGISTRY = DeferredRegister.createBlocks(Omnicraft.ID)

    val MEGA_TORCH by REGISTRY.register("mega_torch",  Supplier<Block> {
        EntityBlockingLightBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_YELLOW)
                .sound(SoundType.WOOD)
                .strength(1.0f, 1.0f)
                .lightLevel { _: BlockState? -> 15 },
            LightTypes.MegaTorch
        )
    })
}
