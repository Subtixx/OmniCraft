package com.github.subtixx.omnicraft.item

import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Block


open class GenericBlockItem(block: Block, properties: Properties) : BlockItem(block, properties) {
    /*override fun appendHoverText(
        pStack: ItemStack,
        pContext: TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag)
        pTooltipComponents.add(Component.translatable(this.getDescriptionId(pStack) + ".tooltip"))
    }*/
}