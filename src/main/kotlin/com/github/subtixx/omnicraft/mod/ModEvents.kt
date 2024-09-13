package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.fluids.BaseMetalFluid
import com.github.subtixx.omnicraft.gen.*
import com.github.subtixx.omnicraft.resources.WorldResource
import com.github.subtixx.omnicraft.tags.*
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.level.block.LiquidBlock
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.extensions.common.ClientExtensionsManager
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
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
        registerGenProviders(pack, registries, existingFileHelper, event)
    }

    private fun registerGenProviders(
        pack: DataGenerator.PackGenerator,
        registries: CompletableFuture<HolderLookup.Provider>,
        existingFileHelper: ExistingFileHelper,
        event: GatherDataEvent
    ) {
        pack.addProvider { packOutput: PackOutput -> RecipeGen(packOutput, registries) }
        pack.addProvider { packOutput: PackOutput -> BlockModelGen(packOutput, existingFileHelper) }
        pack.addProvider { packOutput: PackOutput -> ItemModelGen(packOutput, existingFileHelper) }
        pack.addProvider { packOutput: PackOutput -> LootGen(packOutput, registries) }
        pack.addProvider { packOutput: PackOutput -> LangGen(packOutput) }
        // Generic Json Gen
        event.generator.addProvider(event.includeServer(), DataProvider.Factory { output: PackOutput ->
            GenericGen(
                output,
                event.lookupProvider
            )
        })
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
        pack.addProvider { packOutput: PackOutput -> FluidTagsProvider(packOutput, registries, existingFileHelper) }
    }

    private fun registerIngredients(event: BuildCreativeModeTabContentsEvent) {
        event.accept(ModItems.TINY_COAL_ITEM)
        event.accept(ModItems.TINY_CHARCOAL_ITEM)
    }

    @SubscribeEvent
    fun onTooltip(event: ItemTooltipEvent) {
        ModTools.onTooltip(event)
    }

    // Apply damage to entities in the fluid
    @SubscribeEvent
    fun onEntityTick(event: EntityTickEvent.Post) {
        val entity = event.entity
        if (entity.isRemoved || !entity.isAlive || !entity.isAttackable) return
        val world = entity.level()
        val pos = entity.blockPosition()
        val state = world.getBlockState(pos)

        if (state.block !is LiquidBlock) return
        if(BuiltInRegistries.BLOCK.getKey(state.block).namespace != Omnicraft.ID) return

        val fluid = state.block as LiquidBlock
        val fluidState = state.fluidState
        val fluidType = fluidState.type

        if (fluidType.fluidType.temperature > 1000) {
            entity.hurt(entity.damageSources().lava(), fluidType.fluidType.temperature / 1000f)
        }
    }

    fun register(modEventBus: IEventBus) {
        modEventBus.addListener(ModEvents::gatherData)
        modEventBus.addListener(ModEvents::addCreativeTabs)
        FORGE_BUS.addListener(ModEvents::onEntityTick)

        // Tooltip
        FORGE_BUS.addListener(ModEvents::onTooltip)
    }
}