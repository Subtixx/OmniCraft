package com.github.subtixx.omnicraft.item

import com.github.subtixx.omnicraft.client.BoxHandler
import com.github.subtixx.omnicraft.utils.Platform
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

class TapeItem(properties: Properties) : net.minecraft.world.item.Item(properties) {
    //BoxHandler.clear() -- On left click
    override fun useOn(context: UseOnContext): InteractionResult {
        val player: Player = context.player ?: return super.useOn(context)

        if (Platform.isValidUser(player)) {
            return BoxHandler.addBox(player, context.clickedPos)
        }

        return super.useOn(context)
    }


    override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        if (!Platform.isValidUser(player)) {
            return super.use(level, player, usedHand)
        }

        if (!player.isShiftKeyDown) {
            // TODO: Right click in air -> open gui
            return super.use(level, player, usedHand)
        }

        BoxHandler.undo()
        return super.use(level, player, usedHand)
    }
}
