package com.github.subtixx.omnicraft.light

import net.minecraft.nbt.CompoundTag
import java.util.*

interface ILightSerializer {
    fun serializeLight(light: IEntityBlockingLight): CompoundTag
    fun deserializeLight(nbt: CompoundTag): Optional<IEntityBlockingLight>
    val serializerKey: String
}
