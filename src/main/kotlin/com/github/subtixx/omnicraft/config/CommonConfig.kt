package com.github.subtixx.omnicraft.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class CommonConfig(builder: ModConfigSpec.Builder) {
    init {
        builder.comment("Common Config").push("common")

        builder.pop()
    }

    companion object{
        val spec: ModConfigSpec
        val CONFIG: CommonConfig

        init {
            val specPair: Pair<CommonConfig, ModConfigSpec> =
                ModConfigSpec.Builder().configure { builder: ModConfigSpec.Builder ->
                    CommonConfig(
                        builder
                    )
                }
            spec = specPair.right
            CONFIG = specPair.left
        }
    }
}