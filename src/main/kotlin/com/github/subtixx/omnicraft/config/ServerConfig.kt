package com.github.subtixx.omnicraft.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ServerConfig(builder: ModConfigSpec.Builder) {
    init {
        builder.comment("Server settings").push("server")

        builder.pop()
    }

    companion object {
        val spec: ModConfigSpec
        val CONFIG: ServerConfig

        init {
            val specPair: Pair<ServerConfig, ModConfigSpec> =
                ModConfigSpec.Builder().configure { builder: ModConfigSpec.Builder ->
                    ServerConfig(
                        builder
                    )
                }
            spec = specPair.right
            CONFIG = specPair.left
        }
    }
}