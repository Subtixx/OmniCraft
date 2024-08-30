package com.github.subtixx.omnicraft.item

import com.github.subtixx.omnicraft.Omnicraft
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue
import java.util.function.Supplier

object ModItems {
    val REGISTRY = DeferredRegister.createItems(Omnicraft.ID)

    val TAPE_MEASURE_ITEM by REGISTRY.register("tape_measure", Supplier<Item> {
        TapeItem(Item.Properties())
    })
}