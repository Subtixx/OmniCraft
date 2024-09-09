package com.github.subtixx.omnicraft.energy.storage

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.IntTag
import net.minecraft.nbt.Tag
import net.minecraft.util.Mth
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.energy.IEnergyStorage
import kotlin.math.max
import kotlin.math.min

open class EnergyStorageBase(
    capacity: Int,
    energy: Int = 0,
    maxReceive: Int = capacity,
    maxExtract: Int = capacity
) :
    IEnergyStorage, INBTSerializable<Tag> {
    private var _energy = energy
    public var energy: Int
        get() = _energy
        set(value) {
            _energy = value
            onChange()
        }

    private var _capacity: Int = capacity
    public var capacity: Int
        get() = _capacity
        set(value) {
            _capacity = value
            onChange()
        }

    private var _maxReceive: Int = maxReceive
    public var maxReceive: Int
        get() = _maxReceive
        set(value) {
            _maxReceive = value
            onChange()
        }

    private var _maxExtract: Int = maxExtract
    public var maxExtract: Int
        get() = _maxExtract
        set(value) {
            _maxExtract = value
            onChange()
        }

    constructor(capacity: Int, maxTransfer: Int) : this(capacity, maxTransfer, maxTransfer, 0)

    init {
        this.energy = max(0.0, min(capacity.toDouble(), energy.toDouble())).toInt()
    }

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        if (!canReceive() || toReceive <= 0) {
            return 0
        }

        val energyReceived: Int = Mth.clamp(
            this.capacity - this.energy, 0, min(
                maxReceive.toDouble(), toReceive.toDouble()
            ).toInt()
        )
        if (!simulate) this.energy += energyReceived
        return energyReceived
    }

    override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
        if (!canExtract() || toExtract <= 0) {
            return 0
        }

        val energyExtracted = min(energy.toDouble(), min(maxExtract.toDouble(), toExtract.toDouble()))
            .toInt()
        if (!simulate) this.energy -= energyExtracted
        return energyExtracted
    }

    override fun getEnergyStored(): Int {
        return this.energy
    }

    override fun getMaxEnergyStored(): Int {
        return this.capacity
    }

    override fun canExtract(): Boolean {
        return this.maxExtract > 0
    }

    override fun canReceive(): Boolean {
        return this.maxReceive > 0
    }

    fun serializeNBT(): Tag {
        return IntTag.valueOf(this.energy)
    }

    override fun serializeNBT(provider: HolderLookup.Provider): Tag {
        return IntTag.valueOf(this.energy)
    }

    fun deserializeNBT(nbt: Tag) {
        if (nbt !is IntTag) {
            this.energy = 0
            return
        }

        this.energy = nbt.asInt
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: Tag) {
        if (nbt !is IntTag) {
            this.energy = 0
            return
        }

        this.energy = nbt.asInt
    }

    protected open fun onChange() {}

    open fun setEnergyWithoutUpdate(energy: Int) {
        this._energy = energy
    }

    open fun setCapacityWithoutUpdate(capacity: Int) {
        this._capacity = capacity
    }

    open fun setMaxReceiveWithoutUpdate(maxReceive: Int) {
        this._maxReceive = maxReceive
    }

    open fun setMaxExtractWithoutUpdate(maxExtract: Int) {
        this._maxExtract = maxExtract
    }

    fun getRedstoneSignal(): Int {
        val signal = Mth.floor(energyStored.toFloat() / maxEnergyStored * 14f)
        return min(signal + if (energyStored == 0) 0 else 1, 15)
    }
}
