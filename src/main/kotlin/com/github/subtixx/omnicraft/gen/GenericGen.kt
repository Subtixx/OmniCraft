package com.github.subtixx.omnicraft.gen

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.mod.ModResources
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import java.util.concurrent.CompletableFuture


class GenericGen(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) : DatapackBuiltinEntriesProvider(
    output, registries,
    RegistrySetBuilder(),
    mutableMapOf(),
    mutableSetOf(Omnicraft.ID)
) {
    override fun run(output: CachedOutput): CompletableFuture<*> {
        ModResources.RESOURCES.forEach { (_, resource) ->
            resource.registerOreGen()
        }

        return super.run(output)
    }
}