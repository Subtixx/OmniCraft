package com.github.subtixx.omnicraft

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation

class EntityFilterList
    (private val filterListId: ResourceLocation) {
    private val list: MutableSet<ResourceLocation> = HashSet()

    fun containsEntity(entityName: ResourceLocation): Boolean {
        return list.contains(entityName)
    }

    fun registerEntity(entityName: ResourceLocation) {
        list.add(entityName)
    }

    fun applyListOverrides(overrides: List<String>) {
        for (override in overrides) {
            // minimum len is prefix + valid resource location, i.e. +a:b
            if (override.length < 4) {
                Omnicraft.LOGGER.warn("[{}] Invalid filter definition '{}'", filterListId, override)
                continue
            }

            val prefix = override[0]
            val rl = ResourceLocation.parse(override.substring(1))

            when (prefix) {
                '+' -> if (!this.containsEntity(rl)) {
                    if (!BuiltInRegistries.ENTITY_TYPE.containsKey(rl)) {
                        Omnicraft.LOGGER.warn("[{}] The entity '{}' does not exist, skipping", filterListId, rl)
                        continue
                    }
                    this.registerEntity(rl)
                    Omnicraft.LOGGER.info("[{}] Added '{}' to the block list", filterListId, rl)
                }

                '-' -> if (list.removeIf { rrl: ResourceLocation -> rrl == rl }) {
                    Omnicraft.LOGGER.info("[{}] Removed '{}' from the block list", filterListId, rl)
                }

                else -> Omnicraft.LOGGER.warn(
                    "[{}] Invalid block list prefix: '{}', only + and - are valid prefixes",
                    filterListId,
                    prefix
                )
            }
        }
    }

    val entities: Array<ResourceLocation>
        get() = list.toTypedArray<ResourceLocation>()

    fun clear() {
        list.clear()
    }
}