package com.github.subtixx.omnicraft.tags

import com.github.subtixx.omnicraft.Omnicraft
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import top.theillusivec4.curios.api.CuriosApi
import java.util.concurrent.CompletableFuture

class CurioTagsProvider(
    output: PackOutput,
    providerFuture: CompletableFuture<HolderLookup.Provider>,
    blockLookup: CompletableFuture<TagLookup<Block>>,
    existingFileHelper: ExistingFileHelper?
) :
    ItemTagsProvider(output, providerFuture, blockLookup, Omnicraft.ID, existingFileHelper) {
    override fun getName(): String {
        return "CurioTags"
    }

    override fun addTags(arg: HolderLookup.Provider) {
    }

    object CurioTags {
        val CURIO: TagKey<Item> = tag("curio")

        /**
         * We have to use the curios namespace.
         *
         * @see [Marking Items with Curio
         * Types](https://github.com/TheIllusiveC4/Curios/wiki/How-to-Use:-Developers.marking-items-with-curio-types)
         */
        private fun tag(name: String): TagKey<Item> {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, name))
        }
    }
}
