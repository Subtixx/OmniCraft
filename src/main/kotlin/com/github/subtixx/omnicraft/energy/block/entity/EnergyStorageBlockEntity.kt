package com.github.subtixx.omnicraft.energy.block.entity

import com.github.subtixx.omnicraft.energy.storage.EnergyStorageBase
import com.github.subtixx.omnicraft.energy.storage.IEnergyStoragePacketUpdate
import com.github.subtixx.omnicraft.network.ModMessages
import com.github.subtixx.omnicraft.network.server2client.EnergySyncS2CPacket
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class EnergyStorageBlockEntity<E : EnergyStorageBase>
    (
    type: BlockEntityType<*>,
    blockPos: BlockPos,
    blockState: BlockState,
    protected val baseEnergyCapacity: Int,
    protected val baseEnergyTransferRate: Int
) : BlockEntity(type, blockPos, blockState),
    IEnergyStoragePacketUpdate {
    protected val energyStorage: E

    init {
        energyStorage = initEnergyStorage()
    }

    protected abstract fun initEnergyStorage(): E

    override fun saveAdditional(nbt: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(nbt, registries)

        nbt.put("energy", energyStorage.serializeNBT(registries))
    }

    override fun loadAdditional(nbt: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(nbt, registries)
        if (!nbt.contains("energy")) return

        energyStorage.deserializeNBT(registries, nbt["energy"]!!)
    }

    protected fun syncEnergyToPlayer(player: Player?) {
        ModMessages.sendToPlayer(
            EnergySyncS2CPacket(
                energyStorage.energy, energyStorage.capacity,
                blockPos
            ), player as ServerPlayer
        )
    }

    protected fun syncEnergyToPlayers(distance: Int) {
        if (level == null || level!!.isClientSide()) return

        ModMessages.sendToPlayersWithinXBlocks(
            EnergySyncS2CPacket(energyStorage.energy, energyStorage.capacity, blockPos),
            blockPos, level as ServerLevel, distance
        )
    }

    fun clearEnergy() {
        energyStorage.energy = 0
    }

    override var energy: Int
        get() = energyStorage.energy
        set(newEnergy) {
            energyStorage.setEnergyWithoutUpdate(newEnergy)
        }

    override var capacity: Int
        get() = energyStorage.capacity
        set(newCapacity) {
            energyStorage.setCapacityWithoutUpdate(newCapacity)
        }
}