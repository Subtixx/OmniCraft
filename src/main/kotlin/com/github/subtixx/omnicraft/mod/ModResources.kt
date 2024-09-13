package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.resources.GemWorldResource
import com.github.subtixx.omnicraft.resources.WorldResource
import net.neoforged.neoforge.registries.DeferredRegister

object ModResources {
    val REGISTRY: DeferredRegister.Items = DeferredRegister.createItems(Omnicraft.ID)
    val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(Omnicraft.ID)

    val RESOURCES: Map<String, WorldResource> = mapOf(
        "aluminum" to WorldResource("aluminum", fluidColor = 0xFFD8D8),
        "cinnabar" to GemWorldResource("cinnabar"),
        "bronze" to WorldResource("bronze", addOre = false, fluidColor = 0xDAA520),
        "iridium" to WorldResource("iridium", fluidColor = 0x808080),
        "lead" to WorldResource("lead", fluidColor = 0x404040),
        "nickel" to WorldResource("nickel", fluidColor = 0xbabab3),
        "osmium" to WorldResource("osmium", fluidColor = 0x2423B5),
        "platinum" to WorldResource("platinum", fluidColor = 0x9C9C9C),
        "ruby" to GemWorldResource("ruby"),
        "sapphire" to GemWorldResource("sapphire"),
        "steel" to WorldResource("steel", addOre = false, fluidColor = 0x8C8C8C),
        "silver" to WorldResource("silver", fluidColor = 0xC0C0C0),
        "sulfur" to GemWorldResource("sulfur"),
        "tin" to WorldResource("tin", fluidColor = 0xBDBDBD),
        "titanium" to WorldResource("titanium", fluidColor = 0x7F7F7F),
        "uranium" to WorldResource("uranium", fluidColor = 0x64B612),
        "zinc" to WorldResource("zinc", fluidColor = 0x7F7F7F),
    )

    init {
        RESOURCES.forEach { (_, resource) ->
            resource.registerBlocks(BLOCK_REGISTRY)
            resource.registerItems(REGISTRY)
            resource.registerFluids()
        }
    }
}