package com.github.subtixx.omnicraft.energy.block.entity

import com.github.subtixx.omnicraft.energy.storage.EnergyStorageBase
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState


abstract class MenuEnergyStorageBlockEntity<E : EnergyStorageBase>
    (
    type: BlockEntityType<*>, blockPos: BlockPos, blockState: BlockState,
    protected val machineName: String,
    baseEnergyCapacity: Int, baseEnergyTransferRate: Int
) :
    EnergyStorageBlockEntity<E>(type, blockPos, blockState, baseEnergyCapacity, baseEnergyTransferRate), MenuProvider {
    protected val data: ContainerData

    init {
        data = initContainerData()
    }

    protected open fun initContainerData(): ContainerData {
        return SimpleContainerData(0)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("container.omnicraft.$machineName")
    }
}