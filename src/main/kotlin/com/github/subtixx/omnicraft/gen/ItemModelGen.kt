package com.github.subtixx.omnicraft.gen

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.mod.ModResources
import com.github.subtixx.omnicraft.mod.WorldResource
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ItemModelGen(packOutput: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(packOutput, Omnicraft.ID, existingFileHelper) {
    override fun registerModels() {
        withExistingParent("tiny_coal", "item/generated").texture("layer0", "item/tiny_coal")
        withExistingParent("tiny_charcoal", "item/generated").texture("layer0", "item/tiny_charcoal")

        ModResources.RESOURCES.forEach { (_: String, resource: WorldResource) ->
            resource.registerItemModels(this)
        }
    }
}