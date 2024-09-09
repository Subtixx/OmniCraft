package com.github.subtixx.omnicraft.server

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.level.LevelEvent

object LevelEventHandler {
    @SubscribeEvent
    fun onServerWorldLoaded(event: LevelEvent.Load) {
    }
}