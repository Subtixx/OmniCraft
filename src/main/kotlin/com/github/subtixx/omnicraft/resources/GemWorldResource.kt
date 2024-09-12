package com.github.subtixx.omnicraft.resources

import com.github.subtixx.omnicraft.gen.ItemModelGen
import com.github.subtixx.omnicraft.gen.LootGen
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.*

class GemWorldResource(
    name: String,
    override val addOre: Boolean = true,
    override val addNetherOre: Boolean = true,
    override val addEnderOre: Boolean = true,
    override val addDeepslateOre: Boolean = true,
    override val addNugget: Boolean = false,
    override val addIngot: Boolean = false,
    override val addSmelting: Boolean = false,
    override val addRecipe: Boolean = false,
    override val addRawOre: Boolean = false,
) : WorldResource(name)
{
    var gemItem: DeferredItem<Item>? = null

    override fun registerItems(itemRegister: DeferredRegister.Items) {
        super.registerItems(itemRegister)

        gemItem =
            itemRegister.register(name) { ->
                Item(Item.Properties())
            }
    }

    override fun registerItemModels(itemModelGen: ItemModelGen) {
        super.registerItemModels(itemModelGen)

        itemModelGen.withExistingParent(name, "item/generated")
            .texture("layer0", "item/resources/gem/${name}")
    }

    override fun addCreativeTab(tab: BuildCreativeModeTabContentsEvent) {
        super.addCreativeTab(tab)
        if(tab.tabKey == CreativeModeTabs.INGREDIENTS) {
            tab.accept(gemItem!!.get())
        }
    }

    override fun registerLootTables(lootGen: LootGen.BlockLootSubProvider) {
        super.registerLootTables(lootGen)
        lootGen.add(
            oreBlock!!.get(),
            lootGen.createOreDrop(
                oreBlock!!.get(),
                gemItem!!.get()
            )
        )

        if (addNetherOre) {
            lootGen.add(
                netherOreBlock!!.get(),
                lootGen.createOreDrop(
                    netherOreBlock!!.get(),
                    gemItem!!.get()
                )
            )
        }

        if (addEnderOre) {
            lootGen.add(
                enderOreBlock!!.get(),
                lootGen.createOreDrop(
                    enderOreBlock!!.get(),
                    gemItem!!.get()
                )
            )
        }

        if (addDeepslateOre) {
            lootGen.add(
                deepslateOreBlock!!.get(),
                lootGen.createOreDrop(
                    deepslateOreBlock!!.get(),
                    gemItem!!.get()
                )
            )
        }
    }

    override fun addTranslations(langGen: LanguageProvider) {
        val oreName = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }
        super.addTranslations(langGen)
        langGen.addItem(gemItem!!, oreName)
    }
}