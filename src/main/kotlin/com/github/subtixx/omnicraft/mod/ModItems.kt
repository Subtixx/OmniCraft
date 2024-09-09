package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.item.WrenchItem
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(Omnicraft.ID)

    val WRENCH_ITEM by REGISTRY.register("wrench") { ->
        WrenchItem(
            Item.Properties()
                .stacksTo(1)
        )
    }
}