package com.github.subtixx.omnicraft.menu.block.energy

import com.github.subtixx.omnicraft.energy.block.entity.EnergyStorageBlockEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block


abstract class EnergyStorageMenu<T : EnergyStorageBlockEntity<*>>
protected constructor(
    menuType: MenuType<*>,
    id: Int, playerInventory: Inventory,
    var blockEntity: T,
    protected val blockType: Block,
    playerInventoryX: Int = 8,
    playerInventoryY: Int = 84
) : AbstractContainerMenu(menuType, id),
    IEnergyStorageMenu {
    protected val level: Level = playerInventory.player.level()

    init {
        addPlayerInventorySlots(playerInventory, playerInventoryX, playerInventoryY)
    }

    override val energy: Int
        get() = blockEntity.energy

    override val capacity: Int
        get() = blockEntity.capacity

    override fun stillValid(player: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos), player,
            blockType
        )
    }

    private fun addPlayerInventorySlots(playerInventory: Inventory, x: Int, y: Int) {
        //Player Inventory
        for (i in 0..2) for (j in 0..8) addSlot(Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18))

        //Player Hotbar
        for (i in 0..8) addSlot(Slot(playerInventory, i, x + i * 18, y + 58))
    }
}