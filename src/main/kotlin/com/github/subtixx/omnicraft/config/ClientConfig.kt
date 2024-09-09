package com.github.subtixx.omnicraft.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ClientConfig(builder: ModConfigSpec.Builder) {
    init {
        builder.comment("Client settings").push("client")
        builder.pop()
    }

    companion object{
        val spec: ModConfigSpec
        val CONFIG: ClientConfig

        init {
            val specPair: Pair<ClientConfig, ModConfigSpec> =
                ModConfigSpec.Builder().configure { builder: ModConfigSpec.Builder ->
                    ClientConfig(
                        builder
                    )
                }
            spec = specPair.right
            CONFIG = specPair.left
        }
    }
}