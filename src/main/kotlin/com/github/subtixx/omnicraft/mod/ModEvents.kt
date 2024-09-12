package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.gen.*
import com.github.subtixx.omnicraft.tags.BiomeTagsProvider
import com.github.subtixx.omnicraft.tags.BlockTagsProvider
import com.github.subtixx.omnicraft.tags.CurioTagsProvider
import com.github.subtixx.omnicraft.tags.ItemTagsProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.data.PackOutput
import net.minecraft.world.item.CreativeModeTabs
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import java.util.concurrent.CompletableFuture

object ModEvents {
    @SubscribeEvent
    fun addCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        ModResources.RESOURCES.forEach { (_: String, resource: WorldResource) ->
            resource.addCreativeTab(event)
        }

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

        registerTagProviders(pack, registries, existingFileHelper)
        registerGenProviders(pack, registries, existingFileHelper)
    }

    private fun registerGenProviders(
        pack: DataGenerator.PackGenerator,
        registries: CompletableFuture<HolderLookup.Provider>,
        existingFileHelper: ExistingFileHelper
    ) {
        pack.addProvider { packOutput: PackOutput -> RecipeGen(packOutput, registries) }
        pack.addProvider { packOutput: PackOutput -> BlockModelGen(packOutput, existingFileHelper) }
        pack.addProvider { packOutput: PackOutput -> ItemModelGen(packOutput, existingFileHelper) }
        pack.addProvider { packOutput: PackOutput -> LootGen(packOutput, registries) }
        pack.addProvider { packOutput: PackOutput -> LangGen(packOutput) }
    }

    private fun registerTagProviders(
        pack: DataGenerator.PackGenerator,
        registries: CompletableFuture<HolderLookup.Provider>,
        existingFileHelper: ExistingFileHelper
    ) {
        val blockTagsProvider = pack
            .addProvider { packOutput: PackOutput ->
                BlockTagsProvider(
                    packOutput,
                    registries,
                    existingFileHelper
                )
            }
        pack.addProvider { packOutput: PackOutput ->
            ItemTagsProvider(packOutput, registries, blockTagsProvider.contentsGetter(), existingFileHelper)
        }
        pack.addProvider { packOutput: PackOutput ->
            CurioTagsProvider(
                packOutput,
                registries,
                blockTagsProvider.contentsGetter(),
                existingFileHelper
            )
        }
        pack.addProvider { packOutput: PackOutput -> BiomeTagsProvider(packOutput, registries, existingFileHelper) }

    }

    private fun registerIngredients(event: BuildCreativeModeTabContentsEvent) {
        event.accept(ModItems.TINY_COAL_ITEM)
        event.accept(ModItems.TINY_CHARCOAL_ITEM)
    }

    @SubscribeEvent
    fun onTooltip(event: ItemTooltipEvent) {
        ModTools.onTooltip(event)
    }
}