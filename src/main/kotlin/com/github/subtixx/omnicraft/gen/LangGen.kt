package com.github.subtixx.omnicraft.gen

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.mod.ModResources
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.LanguageProvider

class LangGen(output: PackOutput) : LanguageProvider(
    output,
    Omnicraft.ID,
    "en_us"
) {
    override fun addTranslations() {
        ModResources.RESOURCES.forEach { (_, resource) ->
            resource.addTranslations(this)
        }

        add("item.omnicraft.wrench", "Wrench")
        add("item.omnicraft.tiny_coal", "Tiny Coal",)
        add("item.omnicraft.tiny_charcoal", "Tiny Charcoal")
    }
}