package com.github.subtixx.omnicraft.item

import com.github.subtixx.omnicraft.mod.ModSounds
import com.github.subtixx.omnicraft.utils.BlockUtils
import com.github.subtixx.omnicraft.utils.FluidUtils
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.gameevent.GameEvent
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class WrenchItem(properties: Properties) : Item(properties) {
    override fun getUseAnimation(itemstack: ItemStack): UseAnim {
        return UseAnim.EAT
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val player = context.player ?: return InteractionResult.FAIL
        val level = context.level
        val pos = context.clickedPos
        val itemStack = context.itemInHand
        val dir: Direction = context.clickedFace
        val shiftDown = player.isShiftKeyDown //^ (dir == Direction.DOWN)

        val newDir: Direction? =
            BlockUtils.tryRotatingBlockAndConnected(dir, shiftDown, pos, level, context.clickLocation)
        if (newDir == null) {
            level.playSound(
                context.player, player,
                ModSounds.WRENCH_FAIL, SoundSource.PLAYERS, 1.4f, 0.8f
            )
            return InteractionResult.FAIL
        }

        if (player is ServerPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, itemStack)
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos)
        }
        level.playSound(player, pos, ModSounds.BLOCK_ROTATE, SoundSource.BLOCKS, 1.0F, 1.0F)
        level.playSound(player, player, ModSounds.WRENCH_ROTATE, SoundSource.PLAYERS, 1.0F, 1.4F)
        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    private fun handleFluidHandlerClick(player: Player, blockEntity: IFluidHandler): InteractionResult {
        // Show chat message with infos
        val amounts = FluidUtils.getFluidAmounts(blockEntity)
        for ((key, value) in amounts) {
            player.sendSystemMessage(Component.literal("$key: $value"))
        }
        return InteractionResult.SUCCESS
    }
}