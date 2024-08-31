package com.github.subtixx.omnicraft

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType


class VanillaCompat {
    internal class EntityInfoWrapper(val entityName: ResourceLocation, val entityType: EntityType<*>)
    companion object {
        fun registerTorchEntities(registry: EntityFilterList) {
            // TODO: Fix deprecation warning
            BuiltInRegistries.ENTITY_TYPE.stream()
                .map { entityType: EntityType<*> ->
                    @Suppress("DEPRECATION")
                    EntityInfoWrapper(
                        entityType.builtInRegistryHolder().key().location(), entityType
                    )
                }
                .filter { e: EntityInfoWrapper -> !e.entityType.category.isFriendly }
                .forEach { e: EntityInfoWrapper -> registry.registerEntity(e.entityName) }
        }
    }
}
