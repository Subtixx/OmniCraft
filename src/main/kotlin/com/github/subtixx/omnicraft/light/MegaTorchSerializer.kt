package com.github.subtixx.omnicraft.light

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import java.util.*


class MegaTorchSerializer private constructor(override val serializerKey: String = SERIALIZER_KEY) : ILightSerializer {
    override fun serializeLight(light: IEntityBlockingLight): CompoundTag {
        require(light is MegaTorchEntityBlockingLight) { "Unable to serialize '" + light.javaClass.canonicalName + "', expected '" + MegaTorchEntityBlockingLight::class.java.canonicalName + "'" }

        val nbt = CompoundTag()
        nbt.put("pos", NbtUtils.writeBlockPos(light.pos))

        return nbt
    }

    override fun deserializeLight(nbt: CompoundTag): Optional<IEntityBlockingLight> {
        val pos = NbtUtils.readBlockPos(nbt, "pos")
        return pos.map { blockPos: BlockPos ->
            MegaTorchEntityBlockingLight(
                blockPos
            )
        }
    }

    companion object {
        const val SERIALIZER_KEY: String = "megatorch"

        val INSTANCE: MegaTorchSerializer = MegaTorchSerializer()
    }
}
