package com.github.subtixx.omnicraft.mod

import net.minecraft.data.PackOutput
import net.minecraft.world.item.CreativeModeTabs
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent


object ModEvents {
    @SubscribeEvent
    fun addCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey === CreativeModeTabs.INGREDIENTS) {
            registerIngredients(event)
        }
    }

    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val generator = event.generator
        val pack = generator.getVanillaPack(true)
        val registries = event.lookupProvider
        val existingFileHelper = event.existingFileHelper
        val blockTagsProvider = pack
            .addProvider { packOutput: PackOutput ->
                TagsProvider.Blocks(
                    packOutput,
                    registries,
                    existingFileHelper
                )
            }
        pack.addProvider { packOutput: PackOutput ->
            TagsProvider.Items(packOutput, registries, blockTagsProvider.contentsGetter(), existingFileHelper)
        }

        pack.addProvider { packOutput: PackOutput ->
            TagsProvider.CurioTagsProvider(
                packOutput,
                registries,
                blockTagsProvider.contentsGetter(),
                event.existingFileHelper
            )
        }
        pack.addProvider { packOutput: PackOutput -> RecipeGen(packOutput, registries) }
    }

    private fun registerIngredients(event: BuildCreativeModeTabContentsEvent) {
        event.accept(ModItems.TINY_COAL_ITEM)
        event.accept(ModItems.TINY_CHARCOAL_ITEM)
    }
}