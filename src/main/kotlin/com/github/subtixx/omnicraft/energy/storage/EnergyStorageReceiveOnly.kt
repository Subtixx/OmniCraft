package com.github.subtixx.omnicraft.energy.storage

open class EnergyStorageReceiveOnly(
    capacity: Int,
    maxReceive: Int,
    energy: Int = 0
) : EnergyStorageBase(capacity, maxReceive, 0, energy) {
    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        return 0
    }

    override fun canExtract(): Boolean {
        return false
    }
}