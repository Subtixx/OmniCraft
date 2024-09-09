package com.github.subtixx.omnicraft.server

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent.SpawnPlacementCheck

object SpawnEventHandler {
    @SubscribeEvent
    fun onMobSpawnPlacementCheck(event: SpawnPlacementCheck) {
    }

    @SubscribeEvent
    fun onFinalizeMobSpawn(event: FinalizeSpawnEvent) {
    }
}