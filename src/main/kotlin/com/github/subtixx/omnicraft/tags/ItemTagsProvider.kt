package com.github.subtixx.omnicraft.tags

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.mod.ModResources
import com.github.subtixx.omnicraft.mod.WorldResource
import com.github.subtixx.omnicraft.tags.BlockTagsProvider.Companion
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ItemTagsProvider(
    packOutput: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    provider: CompletableFuture<TagLookup<Block>>,
    existingFileHelper: ExistingFileHelper
) : ItemTagsProvider(packOutput, lookupProvider, provider, Omnicraft.ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {

        // Special copper stuff
        // net.neoforged.neoforge.common.Tags.Items.INGOTS_COPPER
        // net.neoforged.neoforge.common.Tags.Items.ORES_COPPER
        // net.neoforged.neoforge.common.Tags.Items.STORAGE_BLOCKS_COPPER
        // net.neoforged.neoforge.common.Tags.Blocks.ORES_COPPER
        // net.neoforged.neoforge.common.Tags.Blocks.STORAGE_BLOCKS_COPPER
        ModResources.RESOURCES.forEach { (_: String, resource: WorldResource) -> resource.addItemTags(this) }
    }

    public override fun tag(tag: TagKey<Item>): IntrinsicTagAppender<Item> {
        return super.tag(tag)
    }

    companion object {
        //val oresInGroundTag: TagKey<Block> = BlockTagsProvider.forge("ores_in_ground/stone")
        //val ingotBaseTag: TagKey<Item> = com.github.subtixx.omnicraft.tags.ItemTagsProvider.forge("ingots")

        fun forge(tag: String): TagKey<Item> {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", tag))
        }

        fun mod(tag: String): TagKey<Item> {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, tag))
        }
    }
}