package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.util.ExtraCodecs
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier
import java.util.function.UnaryOperator
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModDataComponentTypes {
    val REGISTRY: DeferredRegister<DataComponentType<*>> =
        DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Omnicraft.ID)

    fun <T> registerDataComponentType(
        name: String, builderOperator: Supplier<UnaryOperator<DataComponentType.Builder<T>>>
    ): DeferredHolder<DataComponentType<*>, DataComponentType<T>> {
        return REGISTRY.register(name) { ->
            builderOperator.get().apply(DataComponentType.builder()).build()
        }
    }

    val ENERGY by REGISTRY.register("energy") { ->
        DataComponentType.builder<Int>()
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build()
    }

    val CURRENT_FACE by REGISTRY.register("current_face") { ->
        DataComponentType.builder<Direction>()
            .persistent(Direction.CODEC)
            .networkSynchronized(Direction.STREAM_CODEC)
            .build()
    }

    val ACTION_COOLDOWN by REGISTRY.register("action_cooldown") { ->
        DataComponentType.builder<Int>()
            .persistent(ExtraCodecs.NON_NEGATIVE_INT)
            .networkSynchronized(ByteBufCodecs.VAR_INT)
            .build()
    }
}