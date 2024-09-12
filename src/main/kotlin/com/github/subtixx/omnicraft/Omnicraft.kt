package com.github.subtixx.omnicraft

import com.github.subtixx.omnicraft.config.ClientConfig
import com.github.subtixx.omnicraft.config.CommonConfig
import com.github.subtixx.omnicraft.config.ServerConfig
import com.github.subtixx.omnicraft.mod.*
import net.minecraft.world.item.CreativeModeTabs
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS


@Mod(Omnicraft.ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
class Omnicraft(modEventBus: IEventBus, modContainer: ModContainer) {
    companion object {
        const val ID = "omnicraft"
        val LOGGER: Logger = LogManager.getLogger(ID)
    }

    init {
        modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.spec)
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.spec)
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.spec)

        modEventBus.addListener(ModEvents::gatherData)
        modEventBus.addListener(ModEvents::addCreativeTabs)

        // Tooltip
        FORGE_BUS.addListener(ModEvents::onTooltip)

        ModBlockBehaviors.register()
        ModDataComponentTypes.REGISTRY.register(MOD_BUS)

        ModBlocks.REGISTRY.register(MOD_BUS)
        ModBlocksEnergy.REGISTRY.register(MOD_BUS)

        ModItems.REGISTRY.register(MOD_BUS)
        ModItemsEnergy.REGISTRY.register(MOD_BUS)

        ModResources.BLOCK_REGISTRY.register(MOD_BUS)
        ModResources.REGISTRY.register(MOD_BUS)

        ModBlockEntities.REGISTRY.register(MOD_BUS)
        ModBlockEntitiesEnergy.REGISTRY.register(MOD_BUS)

        ModMaterials.REGISTRY.register(MOD_BUS)

        ModMenus.REGISTRY.register(MOD_BUS)
        ModMenusEnergy.REGISTRY.register(MOD_BUS)

        ModSounds.REGISTRY.register(MOD_BUS)
    }
}
