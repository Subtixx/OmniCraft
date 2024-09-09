package com.github.subtixx.omnicraft.energy.storage

open class EnergyStorageExtractOnly(
    capacity: Int,
    maxExtract: Int,
    energy: Int = 0
) : EnergyStorageBase(
    capacity,
    energy,
    0,
    maxExtract
) {
    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        return 0
    }

    override fun canReceive(): Boolean {
        return false
    }
}