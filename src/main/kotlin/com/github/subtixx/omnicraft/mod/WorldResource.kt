package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.gen.BlockModelGen
import com.github.subtixx.omnicraft.gen.ItemModelGen
import com.github.subtixx.omnicraft.gen.LootGen
import com.github.subtixx.omnicraft.item.GenericBlockItem
import com.github.subtixx.omnicraft.tags.BlockTagsProvider
import com.github.subtixx.omnicraft.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.*

class WorldResource(
    val name: String,
    val addSmelting: Boolean = true,
    val addRecipe: Boolean = true,
    val addOre: Boolean = true,
    val addNetherOre: Boolean = true,
    val addEnderOre: Boolean = true,
    val addDeepslateOre: Boolean = true,
    val smeltExperience: Float = 0.7F,
    val smeltTime: Int = 200
) {
    val oreName: String = name + "_ore"
    var oreBlock: DeferredBlock<Block>? = null
    var oreBlockItem: DeferredItem<BlockItem>? = null

    var netherOreBlock: DeferredBlock<Block>? = null
    var netherOreBlockItem: DeferredItem<BlockItem>? = null
    var enderOreBlock: DeferredBlock<Block>? = null
    var enderOreBlockItem: DeferredItem<BlockItem>? = null
    var deepslateOreBlock: DeferredBlock<Block>? = null
    var deepslateOreBlockItem: DeferredItem<BlockItem>? = null

    var rawOreItem: DeferredItem<Item>? = null
    var storageBlock: DeferredBlock<Block>? = null
    var storageBlockItem: DeferredItem<BlockItem>? = null
    var ingot: DeferredItem<Item>? = null
    var nugget: DeferredItem<Item>? = null

    val oreTag: TagKey<Block> = BlockTagsProvider.forge("ores/$name")
    val oreItemTag: TagKey<Item> = ItemTagsProvider.forge("ores/$name")
    val oreInEndStoneTag: TagKey<Block> = BlockTagsProvider.forge("ores_in_ground/end_stone")
    val oreInEndStoneItemTag: TagKey<Item> = ItemTagsProvider.forge("ores_in_ground/end_stone")
    val ingotTag: TagKey<Item> = ItemTagsProvider.forge("ingots/$name")
    val nuggetTag: TagKey<Item> = ItemTagsProvider.forge("nuggets/$name")
    val storageBlockTag: TagKey<Block> = BlockTagsProvider.forge("storage_blocks/$name")

    fun registerBlocks(blockRegister: DeferredRegister.Blocks) {
        if (addOre) {
            oreBlock =
                blockRegister.register("${name}_ore") { ->
                    Block(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)
                    )
                }
            if (addNetherOre) {
                netherOreBlock =
                    blockRegister.register("${name}_nether_ore") { ->
                        Block(
                            BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_GOLD_ORE)
                        )
                    }
            }

            if (addEnderOre) {
                enderOreBlock =
                    blockRegister.register("${name}_ender_ore") { ->
                        Block(
                            BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE)
                        )
                    }
            }

            if (addDeepslateOre) {
                deepslateOreBlock =
                    blockRegister.register("${name}_deepslate_ore") { ->
                        Block(
                            BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_IRON_ORE)
                        )
                    }
            }
        }

        storageBlock =
            blockRegister.register("${name}_block") { -> Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)) }
    }

    fun registerItems(itemRegister: DeferredRegister.Items) {
        ingot =
            itemRegister.register("${name}_ingot") { -> Item(Item.Properties()) }
        nugget =
            itemRegister.register("${name}_nugget") { -> Item(Item.Properties()) }
        storageBlockItem =
            itemRegister.register("${name}_block") { ->
                GenericBlockItem(storageBlock!!.get(), Item.Properties())
            }
        if (addOre) {
            oreBlockItem =
                itemRegister.register("${name}_ore") { ->
                    GenericBlockItem(
                        oreBlock!!.get(), Item.Properties()
                    )
                }
            rawOreItem =
                itemRegister.register("${name}_raw") { ->
                    Item(Item.Properties())
                }

            if (addNetherOre) {
                netherOreBlockItem =
                    itemRegister.register("${name}_nether_ore") { ->
                        GenericBlockItem(
                            netherOreBlock!!.get(), Item.Properties()
                        )
                    }
            }
            if (addEnderOre) {
                enderOreBlockItem =
                    itemRegister.register("${name}_ender_ore") { ->
                        GenericBlockItem(
                            enderOreBlock!!.get(), Item.Properties()
                        )
                    }
            }
            if (addDeepslateOre) {
                deepslateOreBlockItem =
                    itemRegister.register("${name}_deepslate_ore") { ->
                        GenericBlockItem(
                            deepslateOreBlock!!.get(), Item.Properties()
                        )
                    }
            }
        }
    }

    fun addItemTags(itemTagsProvider: ItemTagsProvider) {
        itemTagsProvider.tag(ingotTag)
            .add(ingot!!.get())
            .replace(false)
        itemTagsProvider.tag(net.neoforged.neoforge.common.Tags.Items.INGOTS)
            .add(ingot!!.get())
            .replace(false)

        itemTagsProvider.tag(nuggetTag)
            .add(nugget!!.get())
            .replace(false)
        itemTagsProvider.tag(net.neoforged.neoforge.common.Tags.Items.NUGGETS)
            .add(nugget!!.get())
            .replace(false)

        itemTagsProvider.tag(net.neoforged.neoforge.common.Tags.Items.STORAGE_BLOCKS)
            .add(storageBlockItem!!.get())
            .replace(false)
        if (addOre) {
            itemTagsProvider.tag(net.neoforged.neoforge.common.Tags.Items.ORES)
                .add(oreBlockItem!!.get())
                .replace(false)
            itemTagsProvider.tag(net.neoforged.neoforge.common.Tags.Items.ORES_IN_GROUND_STONE)
                .add(oreBlockItem!!.get())
                .replace(false)
            itemTagsProvider.tag(oreItemTag)
                .add(oreBlockItem!!.get())
                .replace(false)
            itemTagsProvider.tag(oreItemTag)
                .add(rawOreItem!!.get())
                .replace(false)

            if (addNetherOre) {
                itemTagsProvider.tag(net.neoforged.neoforge.common.Tags.Items.ORES)
                    .add(netherOreBlockItem!!.get())
                    .replace(false)
                itemTagsProvider.tag(net.neoforged.neoforge.common.Tags.Items.ORES_IN_GROUND_NETHERRACK)
                    .add(netherOreBlockItem!!.get())
                    .replace(false)
                itemTagsProvider.tag(oreItemTag)
                    .add(netherOreBlockItem!!.get())
                    .replace(false)
            }
            if (addEnderOre) {
                itemTagsProvider.tag(oreInEndStoneItemTag)
                    .add(enderOreBlockItem!!.get())
                    .replace(false)
                itemTagsProvider.tag(net.neoforged.neoforge.common.Tags.Items.ORES)
                    .add(enderOreBlockItem!!.get())
                    .replace(false)
                itemTagsProvider.tag(oreItemTag)
                    .add(enderOreBlockItem!!.get())
                    .replace(false)
            }
            if (addDeepslateOre) {
                itemTagsProvider.tag(net.neoforged.neoforge.common.Tags.Items.ORES)
                    .add(deepslateOreBlockItem!!.get())
                    .replace(false)
                itemTagsProvider.tag(net.neoforged.neoforge.common.Tags.Items.ORES_IN_GROUND_DEEPSLATE)
                    .add(deepslateOreBlockItem!!.get())
                    .replace(false)
                itemTagsProvider.tag(oreItemTag)
                    .add(deepslateOreBlockItem!!.get())
                    .replace(false)
            }
        }
    }

    fun addBlockTags(blockTagsProvider: BlockTagsProvider) {
        if (addOre) {
            blockTagsProvider.tag(net.neoforged.neoforge.common.Tags.Blocks.ORES)
                .add(oreBlock!!.get())
                .replace(false)
            blockTagsProvider.tag(net.neoforged.neoforge.common.Tags.Blocks.ORES_IN_GROUND_STONE)
                .add(oreBlock!!.get())
                .replace(false)
            blockTagsProvider.tag(oreTag)
                .add(oreBlock!!.get())
                .replace(false)

            if (addNetherOre) {
                blockTagsProvider.tag(net.neoforged.neoforge.common.Tags.Blocks.ORES)
                    .add(netherOreBlock!!.get())
                    .replace(false)
                blockTagsProvider.tag(net.neoforged.neoforge.common.Tags.Blocks.ORES_IN_GROUND_NETHERRACK)
                    .add(netherOreBlock!!.get())
                    .replace(false)
                blockTagsProvider.tag(oreTag)
                    .add(netherOreBlock!!.get())
                    .replace(false)
            }
            if (addEnderOre) {
                blockTagsProvider.tag(net.neoforged.neoforge.common.Tags.Blocks.ORES)
                    .add(enderOreBlock!!.get())
                    .replace(false)
                blockTagsProvider.tag(oreInEndStoneTag)
                    .add(enderOreBlock!!.get())
                    .replace(false)
                blockTagsProvider.tag(oreTag)
                    .add(enderOreBlock!!.get())
                    .replace(false)
            }
            if (addDeepslateOre) {
                blockTagsProvider.tag(net.neoforged.neoforge.common.Tags.Blocks.ORES)
                    .add(deepslateOreBlock!!.get())
                    .replace(false)
                blockTagsProvider.tag(net.neoforged.neoforge.common.Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
                    .add(deepslateOreBlock!!.get())
                    .replace(false)
                blockTagsProvider.tag(oreTag)
                    .add(deepslateOreBlock!!.get())
                    .replace(false)
            }
        }

        blockTagsProvider.tag(net.neoforged.neoforge.common.Tags.Blocks.STORAGE_BLOCKS)
            .add(storageBlock!!.get())
            .replace(false)
        blockTagsProvider.tag(storageBlockTag)
            .add(storageBlock!!.get())
            .replace(false)
    }

    fun registerStatesAndModels(blockModelGen: BlockModelGen) {
        if (addOre && oreBlockItem != null && oreBlock != null) {
            blockModelGen.simpleBlock(
                oreBlock!!.get(),
                blockModelGen.models()
                    .cubeAll(
                        blockModelGen.name(oreBlock!!.get()),
                        blockModelGen.blockTexture(oreBlock!!.get(), "ores/overworld")
                    )
            )

            if (addNetherOre && netherOreBlockItem != null && netherOreBlock != null) {
                blockModelGen.simpleBlock(
                    netherOreBlock!!.get(),
                    blockModelGen.models()
                        .cubeAll(
                            blockModelGen.name(netherOreBlock!!.get()),
                            blockModelGen.simpleBlockTexture(oreName, "ores/nether")
                        )
                )
            }
            if (addEnderOre && enderOreBlockItem != null && enderOreBlock != null) {
                blockModelGen.simpleBlock(
                    enderOreBlock!!.get(),
                    blockModelGen.models()
                        .cubeAll(
                            blockModelGen.name(enderOreBlock!!.get()),
                            blockModelGen.simpleBlockTexture(oreName, "ores/end")
                        )
                )
            }

            if (addDeepslateOre && deepslateOreBlockItem != null && deepslateOreBlock != null) {
                blockModelGen.simpleBlock(
                    deepslateOreBlock!!.get(),
                    blockModelGen.models()
                        .cubeAll(
                            blockModelGen.name(deepslateOreBlock!!.get()),
                            blockModelGen.simpleBlockTexture(oreName, "ores/deepslate")
                        )
                )
            }
        }

        if (storageBlockItem != null && storageBlock != null) {
            blockModelGen.simpleBlock(
                storageBlock!!.get(),
                blockModelGen.models()
                    .cubeAll(
                        blockModelGen.name(storageBlock!!.get()),
                        blockModelGen.blockTexture(storageBlock!!.get(), "storage_blocks")
                    )
            )
        }
    }

    fun registerItemModels(itemModelGen: ItemModelGen) {
        itemModelGen.withExistingParent("${name}_nugget", "item/generated")
            .texture("layer0", "item/nuggets/${name}_nugget")
        itemModelGen.withExistingParent("${name}_ingot", "item/generated")
            .texture("layer0", "item/ingots/${name}_ingot")
        if (addOre) {
            itemModelGen.withExistingParent(
                "${name}_ore",
                ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "block/${name}_ore")
            )
            itemModelGen.withExistingParent("${name}_raw", "item/generated")

            if (addNetherOre) {
                itemModelGen.withExistingParent(
                    "${name}_nether_ore",
                    ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "block/${name}_nether_ore")
                )
            }
            if (addEnderOre) {
                itemModelGen.withExistingParent(
                    "${name}_ender_ore",
                    ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "block/${name}_ender_ore")
                )
            }
            if (addDeepslateOre) {
                itemModelGen.withExistingParent(
                    "${name}_deepslate_ore",
                    ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "block/${name}_deepslate_ore")
                )
            }
        }
        itemModelGen.withExistingParent(
            "${name}_block",
            ResourceLocation.fromNamespaceAndPath(Omnicraft.ID, "block/${name}_block")
        )
    }

    fun addCreativeTab(tab: BuildCreativeModeTabContentsEvent) {
        if (tab.tabKey == CreativeModeTabs.INGREDIENTS) {
            tab.accept(ingot!!.get())
            tab.accept(nugget!!.get())
            if (addOre) {
                tab.accept(rawOreItem!!.get())
            }
        } else if (tab.tabKey == CreativeModeTabs.BUILDING_BLOCKS) {
            tab.accept(storageBlockItem!!.get())
        } else if (tab.tabKey == CreativeModeTabs.NATURAL_BLOCKS) {
            if (addOre) {
                tab.accept(oreBlockItem!!.get())
                if (addNetherOre) {
                    tab.accept(netherOreBlockItem!!.get())
                }
                if (addEnderOre) {
                    tab.accept(enderOreBlockItem!!.get())
                }
                if (addDeepslateOre) {
                    tab.accept(deepslateOreBlockItem!!.get())
                }
            }
        }
    }

    fun registerLootTables(lootGen: LootGen.BlockLootSubProvider) {
        if (oreBlock == null) return

        lootGen.add(
            oreBlock!!.get(),
            lootGen.createOreDrop(
                oreBlock!!.get(),
                rawOreItem!!.get()
            )
        )

        if (netherOreBlock != null) {
            lootGen.add(
                netherOreBlock!!.get(),
                lootGen.createOreDrop(
                    netherOreBlock!!.get(),
                    rawOreItem!!.get()
                )
            )
        }

        if (enderOreBlock != null) {
            lootGen.add(
                enderOreBlock!!.get(),
                lootGen.createOreDrop(
                    enderOreBlock!!.get(),
                    rawOreItem!!.get()
                )
            )
        }

        if (deepslateOreBlock != null) {
            lootGen.add(
                deepslateOreBlock!!.get(),
                lootGen.createOreDrop(
                    deepslateOreBlock!!.get(),
                    rawOreItem!!.get()
                )
            )
        }
    }

    fun addTranslations(langGen: LanguageProvider) {
        val oreName = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }
        if (addOre) {
            langGen.add(oreBlock!!.get(), "$oreName Ore")
            langGen.add(rawOreItem!!.get(), "Raw $oreName")
            if (addNetherOre) {
                langGen.add(netherOreBlock!!.get(), "$oreName Nether Ore")
            }
            if (addEnderOre) {
                langGen.add(enderOreBlock!!.get(), "$oreName Ender Ore")
            }
            if (addDeepslateOre) {
                langGen.add(deepslateOreBlock!!.get(), "$oreName Deepslate Ore")
            }

            langGen.add("tag.block.c.ores.$name", "$oreName Ores")
            langGen.add("tag.item.c.ores.$name", "$oreName Ores")
        }
        langGen.add(storageBlock!!.get(), "$oreName Block")
        langGen.add(ingot!!.get(), "$oreName Ingot")
        langGen.add(nugget!!.get(), "$oreName Nugget")

        langGen.add("tag.item.c.nuggets.$name", "$oreName Nuggets")
        langGen.add("tag.item.c.ingots.$name", "$oreName Ingots")
        langGen.add("tag.item.c.storage_blocks.$name", "$oreName Blocks")
    }
}