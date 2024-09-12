package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import net.neoforged.neoforge.registries.DeferredRegister

object ModResources {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(Omnicraft.ID)
    val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(Omnicraft.ID)

    val RESOURCES: Map<String, WorldResource>

    init {
        RESOURCES = mapOf(
            "silver" to WorldResource("silver"),
            "tin" to WorldResource("tin"),
            "bronze" to WorldResource("bronze", addOre = false),
            "lead" to WorldResource("lead"),
            "nickel" to WorldResource("nickel"),
            "platinum" to WorldResource("platinum"),
            "uranium" to WorldResource("uranium"),
            "titanium" to WorldResource("titanium"),
            "aluminum" to WorldResource("aluminum"),
            "zinc" to WorldResource("zinc"),
        )

        RESOURCES.forEach { (_, resource) ->
            resource.registerBlocks(BLOCK_REGISTRY)
            resource.registerItems(REGISTRY)
        }
    }
}