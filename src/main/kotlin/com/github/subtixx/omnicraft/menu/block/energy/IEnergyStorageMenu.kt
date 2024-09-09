package com.github.subtixx.omnicraft.menu.block.energy

import kotlin.math.max

interface IEnergyStorageMenu {
    val energy: Int
    val capacity: Int

    fun getScaledEnergyMeterPos(energyMeterHeight: Int): Int {
        val energy = energy
        val capacity = capacity

        return if ((energy == 0 || capacity == 0)) 0 else max(1.0, (energy * energyMeterHeight / capacity).toDouble())
            .toInt()
    }

    val energyIndicatorBarValue: Int
        get() = 0

    fun getScaledEnergyIndicatorBarPos(energyMeterHeight: Int): Int {
        return 0
    }
}