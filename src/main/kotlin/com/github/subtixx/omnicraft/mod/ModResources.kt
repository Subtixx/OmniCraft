package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.resources.GemWorldResource
import com.github.subtixx.omnicraft.resources.WorldResource
import net.neoforged.neoforge.registries.DeferredRegister

object ModResources {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(Omnicraft.ID)
    val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(Omnicraft.ID)

    val RESOURCES: Map<String, WorldResource> = mapOf(
        "aluminum" to WorldResource("aluminum"),
        "cinnabar" to GemWorldResource("cinnabar"),
        "bronze" to WorldResource("bronze", addOre = false),
        "iridium" to WorldResource("iridium"),
        "lead" to WorldResource("lead"),
        "nickel" to WorldResource("nickel"),
        "osmium" to WorldResource("osmium"),
        "platinum" to WorldResource("platinum"),
        "ruby" to GemWorldResource("ruby"),
        "sapphire" to GemWorldResource("sapphire"),
        "steel" to WorldResource("steel", addOre = false),
        "silver" to WorldResource("silver"),
        "sulfur" to GemWorldResource("sulfur"),
        "tin" to WorldResource("tin"),
        "titanium" to WorldResource("titanium"),
        "uranium" to WorldResource("uranium"),
        "zinc" to WorldResource("zinc"),
    )

    init {
        RESOURCES.forEach { (_, resource) ->
            resource.registerBlocks(BLOCK_REGISTRY)
            resource.registerItems(REGISTRY)
        }
    }
}