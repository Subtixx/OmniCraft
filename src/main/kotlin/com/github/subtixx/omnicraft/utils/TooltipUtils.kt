package com.github.subtixx.omnicraft.utils

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

object Tooltips {
    @JvmStatic
    fun fluidUnits(mB: Double): MutableComponent {
        return if (mB < 1.0) lessThanOneFluidUnit() else fluidUnits(Math.round(mB).toInt())
    }

    @JvmStatic
    fun fluidUnits(mB: Int): MutableComponent {
        return Component.translatable("omnicraft.tooltip.fluid.units", mB)
    }

    @JvmStatic
    fun fluidUnitsOf(fluid: FluidStack): MutableComponent {
        return Component.translatable("omnicraft.tooltip.fluid.units_of", fluid.amount, fluid.hoverName)
    }

    @JvmStatic
    fun fluidUnitsAndCapacityOf(fluid: FluidStack, capacity: Int): MutableComponent {
        return fluidUnitsAndCapacityOf(fluid.hoverName, fluid.amount, capacity)
    }

    @JvmStatic
    fun fluidUnitsAndCapacityOf(fluid: Component, amount: Int, capacity: Int): MutableComponent {
        return Component.translatable("omnicraft.tooltip.fluid.units_and_capacity_of", amount, capacity, fluid)
    }

    @JvmStatic
    fun lessThanOneFluidUnit(): MutableComponent {
        return Component.translatable("omnicraft.tooltip.fluid.less_than_one_unit")
    }

    @JvmStatic
    fun lessThanOneFluidUnitOf(fluid: Component): MutableComponent {
        return Component.translatable("omnicraft.tooltip.fluid.less_than_one_unit_of", fluid)
    }

    @JvmStatic
    fun lessThanOneFluidUnitOf(fluid: FluidStack): MutableComponent {
        return lessThanOneFluidUnitOf(fluid.hoverName)
    }

    @JvmStatic
    fun countOfItem(stack: ItemStack): MutableComponent {
        return Component.literal(stack.count.toString())
            .append(" x ")
            .append(stack.hoverName)
    }
}