package com.github.subtixx.omnicraft

import com.github.subtixx.omnicraft.Omnicraft.Companion.MegaTorchFilterRegistry
import com.github.subtixx.omnicraft.block.EntityBlockingLightBlock
import com.github.subtixx.omnicraft.client.ClientHandler
import com.github.subtixx.omnicraft.item.ModItems
import net.minecraft.world.item.CreativeModeTabs
import net.neoforged.neoforge.common.NeoForge.EVENT_BUS
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.event.level.LevelEvent
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

abstract class Events {
    companion object {
        fun registerClientEvents() {
            MOD_BUS.addListener(::onClientBuildCreativeModTabContents)

            EVENT_BUS.addListener(ClientHandler::onRenderWorldLast)
            EVENT_BUS.addListener(ClientHandler::onPlayerTick)
            EVENT_BUS.addListener(ClientHandler::onLogIn)
            EVENT_BUS.addListener(ClientHandler::onLogOut)
            EVENT_BUS.addListener(ClientHandler::onRenderGui)

            EVENT_BUS.addListener(EntityBlockingLightBlock::onBlockPlaced)
        }

        fun registerServerEvents() {
            EVENT_BUS.addListener(::onServerWorldLoaded)
            EVENT_BUS.addListener(SpawnEventHandler::onMobSpawnPlacementCheck)
            EVENT_BUS.addListener(SpawnEventHandler::onFinalizeMobSpawn)

            EVENT_BUS.addListener(EntityBlockingLightBlock::onBlockPlaced)
        }

        private fun onServerWorldLoaded(event: LevelEvent.Load) {
            MegaTorchFilterRegistry.clear();
            VanillaCompat.registerTorchEntities(MegaTorchFilterRegistry);
            MegaTorchFilterRegistry.applyListOverrides(OmnicraftConfig.SERVER.megaTorchBlockListOverrides.get())
        }

        private fun onClientBuildCreativeModTabContents(event: BuildCreativeModeTabContentsEvent) {
            if (event.tabKey === CreativeModeTabs.TOOLS_AND_UTILITIES)
            {
                event.accept(ModItems.TAPE_MEASURE_ITEM)
            }
        }
    }
}