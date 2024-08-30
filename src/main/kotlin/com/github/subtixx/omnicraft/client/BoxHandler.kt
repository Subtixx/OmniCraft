package com.github.subtixx.omnicraft.client

import com.github.subtixx.omnicraft.utils.Color
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.phys.AABB
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

object BoxHandler {
    private val boxList: MutableList<MeasurementBox> = ArrayList()
    private var lastDyeColor: Int = 0

    fun addBox(playerEntity: Player, blockPos: BlockPos): InteractionResult {
        // TODO: GUI To select colors.
        val lineColor = Color.getLineColor(lastDyeColor)
        val textColor = Color.getTextColor(lastDyeColor)
        lastDyeColor++
        if (lastDyeColor >= DyeColor.entries.size) {
            lastDyeColor = 0
        }

        if (boxList.isNotEmpty()) {
            // If last box is finished and we have ~64 boxes we need to delete the last one.
            if(boxList[boxList.size - 1].isFinished && boxList.size >= 64) {
                playerEntity.sendSystemMessage(Component.literal("Too many boxes. Deleting last box"))
                boxList.removeAt(boxList.size - 1)
            }
        }
        createOrFinishBox(playerEntity, blockPos, lineColor, lineColor, textColor, textColor, textColor, textColor)

        return InteractionResult.FAIL
    }

    private fun createOrFinishBox(
        playerEntity: Player,
        blockPos: BlockPos,
        lineColor: DyeColor,
        boxColor: DyeColor,
        textColorX: DyeColor,
        textColorY: DyeColor,
        textColorZ: DyeColor,
        textDiagonal: DyeColor
    ) {
        if (boxList.isEmpty()) {
            val box = MeasurementBox(
                blockPos,
                playerEntity.level().dimension(),
                lineColor,
                boxColor,
                textColorX,
                textColorY,
                textColorZ,
                textDiagonal
            )
            boxList.add(box)

            return
        }

        val lastBox = boxList[boxList.size - 1]
        if (lastBox.isFinished) {
            val box = MeasurementBox(
                blockPos, playerEntity.level().dimension(),
                lineColor,
                boxColor,
                textColorX,
                textColorY,
                textColorZ,
                textDiagonal
            )
            boxList.add(box)
        } else {
            lastBox.setBlockEnd(blockPos)
            lastBox.setFinished()
        }
    }

    fun getBoxList(): List<MeasurementBox> {
        return boxList
    }

    fun undo() {
        if (boxList.size > 0) {
            boxList.removeAt(boxList.size - 1)
        }
    }

    fun clear() {
        boxList.clear()
    }
}
