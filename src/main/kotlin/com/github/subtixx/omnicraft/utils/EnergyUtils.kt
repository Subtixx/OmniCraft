@file:Suppress("unused")

package com.github.subtixx.omnicraft.utils

import net.minecraft.util.Mth
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.min

object EnergyUtils {
    @JvmStatic
    fun getRedstoneSignal(energyStorage: IEnergyStorage): Int {
        val energyStored = energyStorage.energyStored
        val maxEnergyStored = energyStorage.maxEnergyStored

        val signal = Mth.floor(energyStored.toFloat() / maxEnergyStored * 14f)
        return min(signal + if (energyStored == 0) 0 else 1, 15)
    }
}