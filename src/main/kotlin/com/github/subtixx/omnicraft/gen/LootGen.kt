package com.github.subtixx.omnicraft.gen

import com.github.subtixx.omnicraft.mod.ModResources
import com.github.subtixx.omnicraft.resources.WorldResource
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.concurrent.CompletableFuture
import java.util.function.Function


class LootGen(
    output: PackOutput,
    registries: CompletableFuture<HolderLookup.Provider>
) :
    LootTableProvider(
        output,
        setOf(),
        listOf(
            SubProviderEntry(
                ::BlockLootSubProvider,
                LootContextParamSets.BLOCK
            )
        ),
        registries
    ) {
    class BlockLootSubProvider(lookupProvider: HolderLookup.Provider) :
        net.minecraft.data.loot.BlockLootSubProvider(setOf<Item>(), FeatureFlags.DEFAULT_FLAGS, lookupProvider) {
        private val knownBlocks: Iterable<Block> = ModResources.RESOURCES.entries.map {
            if(!it.value.addOre) {
                return@map emptyList<Block>()
            }
            val list = mutableListOf(
                it.value.oreBlock!!.get(),
            )

            if(it.value.netherOreBlock != null) {
                list += it.value.netherOreBlock!!.get()
            }

            if(it.value.enderOreBlock != null) {
                list += it.value.enderOreBlock!!.get()
            }

            if(it.value.deepslateOreBlock != null) {
                list += it.value.deepslateOreBlock!!.get()
            }

            return@map list
        }.flatten()

        override fun getKnownBlocks(): Iterable<Block> {
            return knownBlocks
        }

        public override fun createOreDrop(block: Block, item: Item): LootTable.Builder {
            return super.createOreDrop(block, item)
        }

        public override fun add(block: Block, builder: LootTable.Builder) {
            super.add(block, builder)
        }

        public override fun add(block: Block, factory: Function<Block, LootTable.Builder>) {
            super.add(block, factory)
        }

        override fun generate() {
            ModResources.RESOURCES.forEach { (_: String, resource: WorldResource) ->
                resource.registerLootTables(this)
            }
        }
    }

}