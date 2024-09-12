package com.github.subtixx.omnicraft.tags

import com.github.subtixx.omnicraft.Omnicraft
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.FluidTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.FluidTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class FluidTagsProvider(
    packOutput: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) : FluidTagsProvider(packOutput, lookupProvider, Omnicraft.ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
    }
    companion object {
        fun forge(tag: String): TagKey<Fluid> {
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath("c", tag))
        }

        fun mod(tag: String): TagKey<Fluid> {
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, tag))
        }
    }
}