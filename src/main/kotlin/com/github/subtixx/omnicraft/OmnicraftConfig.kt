package com.github.subtixx.omnicraft


import com.github.subtixx.omnicraft.config.LineColor
import com.github.subtixx.omnicraft.config.TextColor
import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

object OmnicraftConfig {
    val clientSpec: ModConfigSpec
    val CLIENT: Client

    init {
        val specPair: Pair<Client, ModConfigSpec> =
            ModConfigSpec.Builder().configure { builder: ModConfigSpec.Builder ->
                Client(
                    builder
                )
            }
        clientSpec = specPair.right
        CLIENT = specPair.left
    }

    class Client internal constructor(builder: ModConfigSpec.Builder) {
        val lineWidth: ModConfigSpec.DoubleValue
        val lineWidthMax: ModConfigSpec.IntValue
        val textSize: ModConfigSpec.DoubleValue
        val lineColor: ModConfigSpec.EnumValue<LineColor>
        val textColor: ModConfigSpec.EnumValue<TextColor>

        init {
            builder.comment("Client settings")
                .push("client")

            lineColor = builder
                .comment("Set line color. [Default: YELLOW]")
                .defineEnum("lineColor", LineColor.YELLOW)
            textColor = builder
                .comment("Set text color. [Default: YELLOW]")
                .defineEnum("textColor", TextColor.YELLOW)
            textSize = builder
                .comment("Set text size [Default: 0.02]")
                .defineInRange("textSize", 0.02, 0.01, 0.10)
            lineWidth = builder
                .comment("Set line width (thickness). [Default: 2]")
                .defineInRange("lineWidth", 2.0, 1.0, 16.0)
            lineWidthMax = builder
                .comment("Set line width when further away (thickness). [Default: 2]")
                .defineInRange("lineWidthMax", 2, 1, 16)

            builder.pop()
        }
    }
}