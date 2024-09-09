package com.github.subtixx.omnicraft.energy.storage

class EnergyStorageInfinite : EnergyStorageBase(
    Int.MAX_VALUE,
    Int.MAX_VALUE,
    Int.MAX_VALUE,
    Int.MAX_VALUE
) {
    override fun getEnergyStored(): Int {
        return Int.MAX_VALUE
    }

    override fun getMaxEnergyStored(): Int {
        return Int.MAX_VALUE
    }

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        return 0
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        return Int.MAX_VALUE
    }

    override fun canReceive(): Boolean {
        return false
    }
}