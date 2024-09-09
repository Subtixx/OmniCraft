package com.github.subtixx.omnicraft.energy.item

import com.github.subtixx.omnicraft.energy.storage.EnergyStorageBase
import com.github.subtixx.omnicraft.utils.NumberUtil
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.neoforged.neoforge.capabilities.Capabilities
import java.util.function.Function
import java.util.function.Supplier
import kotlin.math.max

open class EnergyItemBase(
    properties: Properties,
    private var energyStorageProvider: Function<ItemStack, EnergyStorageBase>
) : Item(properties) {

    constructor(
        props: Properties,
        energyStorageProvider: Supplier<EnergyStorageBase>
    ) : this(props, { _ -> energyStorageProvider.get() })

    protected fun getEnergy(itemStack: ItemStack): Int {
        val energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM)
        return if (energyStorage is ItemCapabilityEnergy) energyStorage.energyStored else 0
    }

    protected fun setEnergy(itemStack: ItemStack, energy: Int) {
        val energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM)
        if (energyStorage is ItemCapabilityEnergy) energyStorage.setEnergy(energy)
    }

    protected fun getCapacity(itemStack: ItemStack): Int {
        val energyStorage = itemStack.getCapability(Capabilities.EnergyStorage.ITEM)
        return if (energyStorage is ItemCapabilityEnergy) energyStorage.maxEnergyStored else 0
    }

    fun getEnergyStorageProvider(): Function<ItemStack, EnergyStorageBase> {
        return energyStorageProvider
    }

    override fun isBarVisible(stack: ItemStack): Boolean {
        return true
    }

    override fun getBarWidth(stack: ItemStack): Int {
        return Math.round(getEnergy(stack) * 13f / getCapacity(stack))
    }

    override fun getBarColor(stack: ItemStack): Int {
        val f = max(0.0, (getEnergy(stack) / getCapacity(stack).toFloat()).toDouble()).toFloat()
        return Mth.hsvToRgb(f * .33f, 1f, 1f)
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        context: TooltipContext,
        components: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        components.add(
            Component.translatable(
                "tooltip.energizedpower.energy_meter.content.txt",
                NumberUtil.formatNumber(getEnergy(itemStack)),
                NumberUtil.formatNumber(getCapacity(itemStack))
            ).withStyle
                (ChatFormatting.GRAY)
        )
    }
}