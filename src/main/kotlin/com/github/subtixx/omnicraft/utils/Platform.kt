package com.github.subtixx.omnicraft.utils

import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.common.util.FakePlayer

object Platform {
    fun isValidUser(player: Player?): Boolean {
        return player != null && player !is FakePlayer && player.level().isClientSide
    }
}