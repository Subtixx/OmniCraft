package com.github.subtixx.omnicraft.utils

import com.github.subtixx.omnicraft.OmnicraftConfig
import com.github.subtixx.omnicraft.config.LineColor
import com.github.subtixx.omnicraft.config.TextColor
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.neoforged.neoforge.common.util.FakePlayer


class Platform {
    companion object {
        fun isValidUser(player: Player?): Boolean {
            return player != null && player !is FakePlayer && player.level().isClientSide
        }

        fun getLineColor(): LineColor {
            return OmnicraftConfig.CLIENT.lineColor.get()
        }

        fun getTextColor(): TextColor {
            return OmnicraftConfig.CLIENT.textColor.get()
        }

        fun getLineWidth(): Float {
            return OmnicraftConfig.CLIENT.lineWidth.get().toFloat()
        }

        fun getLineWidthMax(): Float {
            return OmnicraftConfig.CLIENT.lineWidthMax.get().toFloat()
        }

        fun getTextSize(): Float {
            return OmnicraftConfig.CLIENT.textSize.get().toFloat()
        }
    }
}