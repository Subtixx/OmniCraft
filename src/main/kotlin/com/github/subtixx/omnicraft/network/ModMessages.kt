package com.github.subtixx.omnicraft.network

import com.github.subtixx.omnicraft.network.server2client.EnergySyncS2CPacket
import net.minecraft.core.BlockPos
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.registration.PayloadRegistrar

object ModMessages {
    @SubscribeEvent
    fun register(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar("1.0")

        registerServer2Client(registrar)
        registerClient2Server(registrar)
    }

    private fun registerClient2Server(registrar: PayloadRegistrar) {
    }

    private fun registerServer2Client(registrar: PayloadRegistrar) {        //Server -> Client
        registrar.playToClient(
            EnergySyncS2CPacket.ID,
            EnergySyncS2CPacket.STREAM_CODEC,
            EnergySyncS2CPacket::handle
        )
    }

    @JvmStatic
    fun sendToServer(message: CustomPacketPayload) {
        PacketDistributor.sendToServer(message)
    }

    @JvmStatic
    fun sendToPlayer(message: CustomPacketPayload, player: ServerPlayer) {
        PacketDistributor.sendToPlayer(player, message)
    }

    @JvmStatic
    fun sendToPlayersWithinXBlocks(message: CustomPacketPayload, pos: BlockPos, level: ServerLevel, distance: Int) {
        PacketDistributor.sendToPlayersNear(
            level,
            null,
            pos.x.toDouble(),
            pos.y.toDouble(),
            pos.z.toDouble(),
            distance.toDouble(),
            message
        )
    }

    @JvmStatic
    fun sendToAllPlayers(message: CustomPacketPayload) {
        PacketDistributor.sendToAllPlayers(message)
    }
}
