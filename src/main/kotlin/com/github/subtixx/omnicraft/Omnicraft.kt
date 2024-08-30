package com.github.subtixx.omnicraft

import com.github.subtixx.omnicraft.block.ModBlocks
import com.github.subtixx.omnicraft.client.ClientHandler
import com.github.subtixx.omnicraft.item.ModItems
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModLoadingContext
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.common.NeoForge
import net.neoforged.neoforge.common.NeoForge.EVENT_BUS
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.DeferredRegister
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

@Mod(Omnicraft.ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
class Omnicraft {
    companion object {
        const val ID = "omnicraft"
        // the logger for our mod
        val LOGGER: Logger = LogManager.getLogger(ID)
        // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
        val CREATIVE_MODE_TABS: DeferredRegister<CreativeModeTab> =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ID)
    }

    constructor(modEventBus: IEventBus, modContainer: ModContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, OmnicraftConfig.clientSpec)
    }

    init {
            // Register the KDeferredRegister to the mod-specific event bus
        ModBlocks.REGISTRY.register(MOD_BUS)
        ModItems.REGISTRY.register(MOD_BUS)
        CREATIVE_MODE_TABS.register(MOD_BUS)

        val obj = runForDist(clientTarget = {
            MOD_BUS.addListener(::onClientSetup)
            MOD_BUS.addListener(::addCreative)

            EVENT_BUS.addListener(ClientHandler::onRenderWorldLast)
            EVENT_BUS.addListener(ClientHandler::onPlayerTick)
            EVENT_BUS.addListener(ClientHandler::onLogIn)
            EVENT_BUS.addListener(ClientHandler::onLogOut)
            EVENT_BUS.addListener(ClientHandler::onRenderGui)

            Minecraft.getInstance()
        }, serverTarget = {
            MOD_BUS.addListener(::onServerSetup)
            "test"
        })
        println(obj)
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {
    }

    // Add the example block item to the building blocks tab
    private fun addCreative(event: BuildCreativeModeTabContentsEvent) {
        if (event.tabKey === CreativeModeTabs.TOOLS_AND_UTILITIES)
        {
            event.accept(ModItems.TAPE_MEASURE_ITEM)
        }
    }
}
