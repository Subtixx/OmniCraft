package com.github.subtixx.omnicraft

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent.SpawnPlacementCheck
import net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent;
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

class SpawnEventHandler {
    companion object {
        @SubscribeEvent
        fun onMobSpawnPlacementCheck(event: SpawnPlacementCheck) {
            // MobSpawnEvent. FinalizeSpawn.setSpawnCancelled

            EntityBlockLightManager.getRegistryForLevel(event.level.level).ifPresent { reg: EntityBlockLightManager ->
                if (reg.shouldBlockEntity(event.entityType, event.pos.toVec3(), event.level.level, event.spawnType)) {
                    event.result = SpawnPlacementCheck.Result.FAIL
                }
            }
        }

        @SubscribeEvent
        fun onFinalizeMobSpawn(event: FinalizeSpawnEvent) {
            //event.isSpawnCancelled = true
        }
    }
}