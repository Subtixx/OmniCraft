package com.github.subtixx.omnicraft.network.server2client

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.energy.storage.IEnergyStoragePacketUpdate
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.network.handling.IPayloadContext

@JvmRecord
data class EnergySyncS2CPacket(
    val energy: Int,
    val capacity: Int,
    val pos: BlockPos
) : CustomPacketPayload {
    constructor(buffer: RegistryFriendlyByteBuf) : this(buffer.readInt(), buffer.readInt(), buffer.readBlockPos())

    fun write(buffer: RegistryFriendlyByteBuf) {
        buffer.writeInt(energy)
        buffer.writeInt(capacity)
        buffer.writeBlockPos(pos)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return ID
    }

    companion object {
        val ID: CustomPacketPayload.Type<EnergySyncS2CPacket> = CustomPacketPayload.Type<EnergySyncS2CPacket>(
            ResourceLocation.fromNamespaceAndPath(
                Omnicraft.ID, "energy_sync"
            )
        )
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EnergySyncS2CPacket> =
            StreamCodec.ofMember({ obj: EnergySyncS2CPacket, buffer: RegistryFriendlyByteBuf -> obj.write(buffer) },
                { buffer: RegistryFriendlyByteBuf -> EnergySyncS2CPacket(buffer) })

        fun handle(data: EnergySyncS2CPacket, context: IPayloadContext) {
            context.enqueueWork {
                val blockEntity = context.player().level().getBlockEntity(data.pos)
                if (blockEntity !is IEnergyStoragePacketUpdate) {
                    return@enqueueWork
                }

                val energyStorage = blockEntity as IEnergyStoragePacketUpdate
                energyStorage.capacity = data.capacity
                energyStorage.energy = data.energy
            }
        }
    }
}