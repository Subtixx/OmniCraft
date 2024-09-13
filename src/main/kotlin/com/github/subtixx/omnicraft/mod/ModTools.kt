package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.utils.encodeStack
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent


object ModTools {
    /**
     * Function to display NBT data on item tooltips used for debugging
     *
     * @param event
     */
    fun onTooltip(event: ItemTooltipEvent) {
        if(!Minecraft.getInstance().options.advancedItemTooltips) {
            return
        }
        val stack = event.itemStack

        event.toolTip.add(Component.empty())

        event.toolTip.add(
            Component.literal("Tags:")
                .withStyle(ChatFormatting.DARK_PURPLE)
        )
        for (tag in stack.tags) {
            event.toolTip.add(
                Component.literal("    ${tag.registry.location()}    |    ${tag.location}")
                    .withStyle(ChatFormatting.GRAY)
            )
        }

        event.toolTip.add(Component.empty())
        event.toolTip.add(
            Component.literal("NBT:")
                .withStyle(ChatFormatting.DARK_PURPLE)
        )
        val context = event.context
        if (context.registries() != null) {
            val tag = stack.encodeStack(context.registries())
            if (tag != null) {
                event.toolTip.add(
                    Component.literal("    $tag")
                        .withStyle(ChatFormatting.GRAY)
                )
            }
        }
    }
}