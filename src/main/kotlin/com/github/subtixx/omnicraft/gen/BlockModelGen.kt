package com.github.subtixx.omnicraft.gen

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.mod.ModResources
import com.github.subtixx.omnicraft.resources.WorldResource
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class BlockModelGen(packOutput: PackOutput, existingFileHelper: ExistingFileHelper) :
    BlockStateProvider(packOutput, Omnicraft.ID, existingFileHelper) {
    override fun registerStatesAndModels() {
        ModResources.RESOURCES.forEach { (_: String, resource: WorldResource) ->
            resource.registerStatesAndModels(this)
        }
    }

    fun name(block: Block): String {
        return key(block).path
    }

    fun key(block: Block): ResourceLocation {
        return BuiltInRegistries.BLOCK.getKey(block)
    }

    fun blockTexture(block: Block, subfolder: String = ""): ResourceLocation {
        val name = key(block)
        return ResourceLocation.fromNamespaceAndPath(
            name.namespace,
            ModelProvider.BLOCK_FOLDER + "/" + subfolder + "/" + name.path
        )
    }

    fun simpleBlockTexture(name: String, subfolder: String = ""): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(
            Omnicraft.ID,
            ModelProvider.BLOCK_FOLDER + "/" + subfolder + "/" + name
        )
    }

    fun itemTexture(name: String, subfolder: String = ""): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(
            Omnicraft.ID,
            ModelProvider.ITEM_FOLDER + "/" + subfolder + "/" + name
        )
    }
}