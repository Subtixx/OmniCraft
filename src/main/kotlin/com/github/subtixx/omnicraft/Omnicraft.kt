package com.github.subtixx.omnicraft

import com.github.subtixx.omnicraft.block.ModBlocks
import com.github.subtixx.omnicraft.client.ClientHandler
import com.github.subtixx.omnicraft.item.ModItems
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.common.NeoForge.EVENT_BUS
import net.neoforged.neoforge.registries.DeferredRegister
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist


@Mod(Omnicraft.ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
class Omnicraft(modEventBus: IEventBus, modContainer: ModContainer) {
    companion object {
        const val ID = "omnicraft"

        // the logger for our mod
        val LOGGER: Logger = LogManager.getLogger(ID)

        // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
        val CREATIVE_MODE_TABS: DeferredRegister<CreativeModeTab> =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ID)

        val MegaTorchFilterRegistry: EntityFilterList =
            EntityFilterList(ResourceLocation.fromNamespaceAndPath(ID, "entity_filter/mega_torch"))
    }

    init {
        modContainer.registerConfig(ModConfig.Type.CLIENT, OmnicraftConfig.clientSpec)
        modContainer.registerConfig(ModConfig.Type.SERVER, OmnicraftConfig.serverSpec)

        ModBlocks.REGISTRY.register(MOD_BUS)
        ModItems.REGISTRY.register(MOD_BUS)

        CREATIVE_MODE_TABS.register(MOD_BUS)

        runForDist(clientTarget = {
            Events.registerClientEvents()
        }, serverTarget = {
            Events.registerServerEvents()
        })
    }
}
