package com.github.subtixx.omnicraft.utils

import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import kotlin.math.min

object FluidUtils {
    fun getFluidAmount(fluidHandler: IFluidHandler, tankIndex: Int = -1): String {
        if (fluidHandler.tanks == 0) {
            return Component.translatable("omnicraft.fluid.less_than_one_unit").string
        }
        var totalAmount = fluidHandler.getFluidInTank(tankIndex).amount
        if (tankIndex != -1 && tankIndex < fluidHandler.tanks) {
            totalAmount = 0
            for (i in 0 until fluidHandler.tanks) {
                totalAmount += fluidHandler.getFluidInTank(i).amount
            }
        }

        return Component.translatable(
            "omnicraft.fluid.units",
            totalAmount
        ).string
    }

    fun getFluidAmount(fluidStack: FluidStack, withFluidName: Boolean = true): String {
        return when {
            !withFluidName -> {
                Component.translatable("omnicraft.fluid.units", fluidStack.amount).string
            }

            else -> Component.translatable(
                "omnicraft.fluid.units_of",
                fluidStack.amount,
                fluidStack.hoverName
            ).string
        }
    }

    fun getFluidAmounts(fluidHandler: IFluidHandler): HashMap<String, Int> {
        val map = HashMap<String, Int>()
        for (i in 0 until fluidHandler.tanks) {
            val fluid = fluidHandler.getFluidInTank(i)
            map[fluid.hoverName.string] = fluid.amount
        }
        return map
    }

    fun getRedstoneSignal(fluidHandler: IFluidHandler, tankIndex: Int = -1): Int {
        var fullnessPercentSum = 0f
        var isEmptyFlag = true

        val size = fluidHandler.tanks
        if (tankIndex != -1 && tankIndex < size) {
            val fluid = fluidHandler.getFluidInTank(tankIndex)
            if (!fluid.isEmpty) {
                fullnessPercentSum = fluid.amount.toFloat() / fluidHandler.getTankCapacity(tankIndex)
                isEmptyFlag = false
            }
        } else {
            for (i in 0 until size) {
                val fluid = fluidHandler.getFluidInTank(i)
                if (!fluid.isEmpty) {
                    fullnessPercentSum += fluid.amount.toFloat() / fluidHandler.getTankCapacity(i)
                    isEmptyFlag = false
                }
            }
        }

        return min((Mth.floor(fullnessPercentSum / size * 14f) + (if (isEmptyFlag) 0 else 1)).toDouble(), 15.0).toInt()
    }
}