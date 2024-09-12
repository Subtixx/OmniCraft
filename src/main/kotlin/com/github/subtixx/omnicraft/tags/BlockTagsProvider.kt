package com.github.subtixx.omnicraft.tags

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.mod.ModResources
import com.github.subtixx.omnicraft.mod.WorldResource
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class BlockTagsProvider(
    packOutput: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper
) : net.neoforged.neoforge.common.data.BlockTagsProvider(packOutput, lookupProvider, Omnicraft.ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        tag(NEEDS_COPPER_TOOL)
            .addTag(BlockTags.NEEDS_STONE_TOOL)
        tag(NEEDS_BRONZE_TOOL)
            .addTag(BlockTags.NEEDS_STONE_TOOL)
        tag(NEEDS_STEEL_TOOL)
            .addTag(BlockTags.NEEDS_DIAMOND_TOOL)
        tag(INCORRECT_FOR_COPPER_TOOL)
            .addTag(BlockTags.NEEDS_IRON_TOOL)
        tag(INCORRECT_FOR_BRONZE_TOOL)
            .addTag(BlockTags.NEEDS_IRON_TOOL)
        tag(INCORRECT_FOR_STEEL_TOOL)
            .addTag(BlockTags.NEEDS_IRON_TOOL)

        ModResources.RESOURCES.forEach { (_: String, resource: WorldResource) -> resource.addBlockTags(this) }
    }

    public override fun tag(tag: TagKey<Block>): IntrinsicTagAppender<Block> {
        return super.tag(tag)
    }

    companion object {
        val NEEDS_COPPER_TOOL: TagKey<Block> = TagKey.create(
            BuiltInRegistries.BLOCK.key(),
            ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "needs_copper_tool")
        )
        val INCORRECT_FOR_COPPER_TOOL: TagKey<Block> = TagKey.create(
            BuiltInRegistries.BLOCK.key(),
            ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "incorrect_for_copper_tool")
        )
        val NEEDS_BRONZE_TOOL: TagKey<Block> = TagKey.create(
            BuiltInRegistries.BLOCK.key(),
            ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "needs_bronze_tool")
        )
        val INCORRECT_FOR_BRONZE_TOOL: TagKey<Block> = TagKey.create(
            BuiltInRegistries.BLOCK.key(),
            ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "incorrect_for_bronze_tool")
        )
        val NEEDS_STEEL_TOOL: TagKey<Block> = TagKey.create(
            BuiltInRegistries.BLOCK.key(),
            ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "needs_steel_tool")
        )
        val INCORRECT_FOR_STEEL_TOOL: TagKey<Block> = TagKey.create(
            BuiltInRegistries.BLOCK.key(),
            ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "incorrect_for_steel_tool")
        )

        fun forge(tag: String): TagKey<Block> {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", tag))
        }

        fun mod(tag: String): TagKey<Block> {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, tag))
        }
    }
}