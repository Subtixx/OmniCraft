package com.github.subtixx.omnicraft


import com.github.subtixx.omnicraft.config.LineColor
import com.github.subtixx.omnicraft.config.TextColor
import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair
import org.spongepowered.asm.mixin.Mutable
import java.util.function.Predicate
import java.util.function.Supplier


object OmnicraftConfig {
    val clientSpec: ModConfigSpec
    val CLIENT: ClientConfig

    val serverSpec: ModConfigSpec
    val SERVER: ServerConfig

    init {
        val specPair: Pair<ClientConfig, ModConfigSpec> =
            ModConfigSpec.Builder().configure { builder: ModConfigSpec.Builder ->
                ClientConfig(
                    builder
                )
            }
        clientSpec = specPair.right
        CLIENT = specPair.left

        val specPair2: Pair<ServerConfig, ModConfigSpec> =
            ModConfigSpec.Builder().configure { builder: ModConfigSpec.Builder ->
                ServerConfig(
                    builder
                )
            }
        serverSpec = specPair2.right
        SERVER = specPair2.left
    }

    class ServerConfig internal constructor(builder: ModConfigSpec.Builder) {
        val megaTorchRadius: ModConfigSpec.IntValue
        val megaTorchBlockListOverrides: ModConfigSpec.ConfigValue<MutableList<out String>>

        init {
            builder.comment("Server settings").push("server")

            megaTorchRadius = builder.comment("Set radius of mega torch. [Default: 64]")
                .defineInRange("megaTorchRadius", 64, 1, Int.MAX_VALUE)
            megaTorchBlockListOverrides = builder.comment(
                "Use this setting to override the internal lists for entity blocking",
                "You can use this to block more entities or even allow certain entities to still spawn",
                "The + prefix will add the entity to the list, effectivly denying its spawns",
                "The - prefix will remove the entity from the list (if necessary), effectivly allowing its spawns",
                "Note: Each entry needs to be put in quotes! Multiple Entries should be separated by comma.",
                "Block zombies: \"+minecraft:zombie\"",
                "Allow creepers: \"-minecraft:creeper\""
            ).translation("torchmaster.config.megaTorch.blockListOverrides.description")
                .defineList("megaTorchEntityBlockListOverrides", ArrayList<String>(), { "" }, { it is String })

            builder.pop()
        }
    }

    class ClientConfig internal constructor(builder: ModConfigSpec.Builder) {
        val lineWidth: ModConfigSpec.DoubleValue
        val lineWidthMax: ModConfigSpec.IntValue
        val textSize: ModConfigSpec.DoubleValue
        val lineColor: ModConfigSpec.EnumValue<LineColor>
        val textColor: ModConfigSpec.EnumValue<TextColor>

        init {
            builder.comment("Client settings").push("client")

            lineColor = builder.comment("Set line color. [Default: YELLOW]").defineEnum("lineColor", LineColor.YELLOW)
            textColor = builder.comment("Set text color. [Default: YELLOW]").defineEnum("textColor", TextColor.YELLOW)
            textSize = builder.comment("Set text size [Default: 0.02]").defineInRange("textSize", 0.02, 0.01, 0.10)
            lineWidth =
                builder.comment("Set line width (thickness). [Default: 2]").defineInRange("lineWidth", 2.0, 1.0, 16.0)
            lineWidthMax = builder.comment("Set line width when further away (thickness). [Default: 2]")
                .defineInRange("lineWidthMax", 2, 1, 16)

            builder.pop()
        }
    }
}