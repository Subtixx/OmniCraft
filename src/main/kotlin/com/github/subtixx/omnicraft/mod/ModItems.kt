package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.item.TinyCoalItem
import com.github.subtixx.omnicraft.item.WrenchItem
import net.minecraft.world.item.BucketItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.FlowingFluid
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModItems {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(Omnicraft.ID)

    val WRENCH_ITEM: WrenchItem by REGISTRY.register("wrench") { ->
        WrenchItem(
            Item.Properties()
                .stacksTo(1)
        )
    }

    val TINY_COAL_ITEM: Item by REGISTRY.register("tiny_coal") { ->
        TinyCoalItem(Item.Properties())
    }

    val TINY_CHARCOAL_ITEM: Item by REGISTRY.register("tiny_charcoal") { ->
        TinyCoalItem(Item.Properties())
    }

    fun createBucketItem(name: String, fluid: DeferredHolder<Fluid, FlowingFluid>): DeferredItem<BucketItem> {
        return REGISTRY.register(name + "_bucket") { ->
            BucketItem(
                fluid.get(),
                Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1)
            )
        }
    }
}