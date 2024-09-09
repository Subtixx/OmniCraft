package com.github.subtixx.omnicraft.energy.item

import com.github.subtixx.omnicraft.energy.storage.EnergyStorageBase
import com.github.subtixx.omnicraft.mod.ModDataComponentTypes
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.Tag
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.energy.IEnergyStorage

class ItemCapabilityEnergy(
    private val itemStack: ItemStack,
    private val energyStorage: EnergyStorageBase
) :
    IEnergyStorage {

    init {
        if (itemStack.has(ModDataComponentTypes.ENERGY)) this.energyStorage.deserializeNBT(
            IntTag.valueOf(
                itemStack.getOrDefault(ModDataComponentTypes.ENERGY, 0)
            )
        )
    }

    override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
        val ret: Int = energyStorage.receiveEnergy(maxReceive, simulate)

        if (!simulate) {
            val nbt: Tag = energyStorage.serializeNBT()
            if (nbt is IntTag) itemStack.set(ModDataComponentTypes.ENERGY, nbt.asInt)
        }

        return ret
    }

    override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int {
        val ret: Int = energyStorage.extractEnergy(maxExtract, simulate)

        if (!simulate) {
            val nbt: Tag = energyStorage.serializeNBT()
            if (nbt is IntTag) itemStack.set(ModDataComponentTypes.ENERGY, nbt.asInt)
        }

        return ret
    }

    override fun getEnergyStored(): Int {
        return energyStorage.energyStored
    }

    override fun getMaxEnergyStored(): Int {
        return energyStorage.maxEnergyStored
    }

    override fun canExtract(): Boolean {
        return energyStorage.canExtract()
    }

    override fun canReceive(): Boolean {
        return energyStorage.canReceive()
    }

    fun setEnergy(energy: Int) {
        energyStorage.energy = energy

        val nbt: Tag = energyStorage.serializeNBT()
        if (nbt is IntTag) itemStack.set(ModDataComponentTypes.ENERGY, nbt.asInt)
    }

    fun setCapacity(capacity: Int) {
        energyStorage.capacity = capacity
    }

    fun getEnergyStorage(): EnergyStorageBase {
        return energyStorage
    }
}
