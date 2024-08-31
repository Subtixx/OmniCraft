package com.github.subtixx.omnicraft

import com.github.subtixx.omnicraft.light.IEntityBlockingLight
import com.github.subtixx.omnicraft.light.ILightSerializer
import com.github.subtixx.omnicraft.light.LightSerializerRegistry
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3
import java.util.*

class EntityBlockLightManager : SavedData() {
    private val lights: HashMap<String, IEntityBlockingLight> = HashMap()

    fun shouldBlockEntity(entityType: EntityType<*>, spawnPosition: Vec3, level: Level, spawnType: MobSpawnType): Boolean {
        for (light in lights.values) {
            if (light.shouldBlockEntity(entityType, spawnPosition, level, spawnType)) {
                return true
            }
        }
        return false
    }

    fun shouldBlockVillagePillagerSiege(pos: Vec3): Boolean {
        for (light in lights.values) {
            if (light.shouldBlockVillagePillagerSiege(pos)) {
                return true
            }
        }
        return false
    }

    fun shouldBlockVillageZombieRaid(pos: Vec3): Boolean {
        for (light in lights.values) {
            if (light.shouldBlockVillageZombieRaid(pos)) {
                return true
            }
        }
        return false
    }

    fun registerLight(lightKey: String, light: IEntityBlockingLight) {
        lights[lightKey] = light
        setDirty()
    }

    fun unregisterLight(lightKey: String?) {
        lights.remove(lightKey)
        setDirty()
    }

    fun getLight(lightKey: String): Optional<IEntityBlockingLight> {
        val light = lights[lightKey] ?: return Optional.empty()
        return Optional.of(light)
    }

    fun onGlobalTick(level: Level) {
        // TODO: Rate limit this
        for (light in lights.values) {
            light.cleanupCheck(level)
        }
    }

    override fun save(compoundTag: CompoundTag, provider: HolderLookup.Provider): CompoundTag {
        val list = ListTag()
        lights.entries.forEach { (lightKey, light) ->
            val serializerType = light.lightSerializerType
            LightSerializerRegistry.getLightSerializer(serializerType).ifPresentOrElse({ serializer: ILightSerializer ->
                val tag = serializer.serializeLight(light)
                tag.putString("_type", serializerType)
                tag.putString("_key", lightKey)
                list.add(tag)
            }, {
                Omnicraft.LOGGER.error("Unable to save light {}, data is lost", light.pos)
            })
        }
        return CompoundTag().apply {
            put("lights", list)
        }
    }

    fun isRegistered(lightTypeName: String, pos: BlockPos): Boolean {
        for (entry in lights) {
            if(entry.value.boundingBox.contains(pos.toVec3())) {
                return true
            }
        }
        return false
    }

    companion object {
        fun getRegistryForLevel(level: Level): Optional<EntityBlockLightManager> {
            if (level !is ServerLevel) {
                return Optional.empty()
            }

            val dimensionIdentifier = level.dimension().location().toDebugFileName()
            return Optional.of(
                level.dataStorage.computeIfAbsent(
                    Factory,
                    "omnicraft_lights_$dimensionIdentifier"
                )
            )
        }

        private fun load(tag: CompoundTag, provider: HolderLookup.Provider): EntityBlockLightManager {
            val lightsList = tag.getList("lights", Tag.TAG_COMPOUND.toInt())
            val mgr = EntityBlockLightManager()
            mgr.lights.clear()
            for (i in lightsList.indices) {
                val lightNbt = lightsList.getCompound(i)
                val lightKey = lightNbt.getString("_key")
                val serializerType = lightNbt.getString("_type")
                LightSerializerRegistry.getLightSerializer(serializerType)
                    .ifPresentOrElse({ serializer: ILightSerializer ->
                        serializer.deserializeLight(lightNbt)
                            .ifPresentOrElse({ l: IEntityBlockingLight -> mgr.lights[lightKey] = l }, {
                                Omnicraft.LOGGER.error(
                                    "Unable to load light data from nbt for {} - {}, deserialization failed, data is lost",
                                    lightKey,
                                    serializerType
                                )
                            })
                    }, {
                        Omnicraft.LOGGER.error(
                            "Unable to load light data from nbt for {} - {}. Serializer not found, data is lost",
                            lightKey,
                            serializerType
                        )
                    })
            }
            return mgr
        }

        val Factory: Factory<EntityBlockLightManager> = Factory<EntityBlockLightManager>(
            { EntityBlockLightManager() },
            { obj: CompoundTag, tag: HolderLookup.Provider? ->
                load(obj, tag!!)
            },
            null
        )
    }
}