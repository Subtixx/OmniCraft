package com.github.subtixx.omnicraft.tags

import com.github.subtixx.omnicraft.Omnicraft
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.BiomeTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.biome.Biome
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class BiomeTagsProvider(
    packOutput: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) : BiomeTagsProvider(packOutput, lookupProvider, Omnicraft.ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
    }

    companion object {
        fun forge(tag: String): TagKey<Biome> {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", tag))
        }

        fun mod(tag: String): TagKey<Biome> {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, tag))
        }
    }
}