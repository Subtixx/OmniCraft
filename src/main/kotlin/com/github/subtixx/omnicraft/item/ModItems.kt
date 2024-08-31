package com.github.subtixx.omnicraft.item

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.block.EntityBlockingLightBlock
import com.github.subtixx.omnicraft.block.ModBlocks
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import java.util.function.Supplier

object ModItems {
    val REGISTRY = DeferredRegister.createItems(Omnicraft.ID)

    val TAPE_MEASURE_ITEM by REGISTRY.register("tape_measure", Supplier<Item> {
        TapeItem(Item.Properties())
    })

    val MEGA_TORCH_ITEM by REGISTRY.register("mega_torch", Supplier<Item> {
        LightBlockItem(ModBlocks.MEGA_TORCH, Item.Properties())
    })
}