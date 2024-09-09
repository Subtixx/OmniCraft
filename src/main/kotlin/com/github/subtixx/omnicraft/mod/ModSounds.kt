package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier
import thedarkcolour.kotlinforforge.neoforge.forge.getValue

object ModSounds {
    val REGISTRY: DeferredRegister<SoundEvent> = DeferredRegister.create(Registries.SOUND_EVENT, Omnicraft.ID)
    val BLOCK_ROTATE: SoundEvent by REGISTRY.register("block.rotate") { ->
        SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "block.rotate"))
    }
    val WRENCH_ROTATE: SoundEvent by REGISTRY.register("item.wrench.rotate") { ->
        SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "item.wrench.rotate"))
    }
    val WRENCH_FAIL: SoundEvent by REGISTRY.register("item.wrench.fail") { ->
        SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "item.wrench.fail"))
    }
}