package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import com.github.subtixx.omnicraft.fluids.BaseMetalFluid
import com.github.subtixx.omnicraft.fluids.SteamFluid
import com.github.subtixx.omnicraft.resources.WorldResource
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Fluid
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.event.tick.EntityTickEvent
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.FluidType.Properties
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import thedarkcolour.kotlinforforge.neoforge.forge.getValue


object ModFluids {
    val FLUIDS: DeferredRegister<Fluid> = DeferredRegister.create(BuiltInRegistries.FLUID, Omnicraft.ID)
    val FLUID_TYPES: DeferredRegister<FluidType> = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, Omnicraft.ID)

    val STEAM by FLUIDS.register("steam") { -> SteamFluid() }
    val STEAM_TYPE: FluidType by FLUID_TYPES.register("steam") { ->
        FluidType(
            Properties.create()
                .temperature(400) // in kelvin, 150c
                .density(-1000)
                .viscosity(500)
        )
    }

    /*val GAS_FLUID = FluidType(
        Properties.create()
            .pathType(PathType.LAVA)
            .canSwim(false)
            //.gaseous(true)
            .density(-10)
            .viscosity(40)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.FIRE_EXTINGUISH)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH)
    )

    val HOT_GAS_FLUID = FluidType(
        Properties.create()
            .pathType(PathType.LAVA)
            .density(-10)
            //.gaseous(true)
            .viscosity(40)
            .temperature(1000)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.FIRE_EXTINGUISH)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH)
    )

    val STEAM_TYPE = FluidType(
        Properties.create()
            .pathType(PathType.LAVA)
            .density(-10)
            //.gaseous(true)
            .viscosity(40)
            .temperature(450)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.FIRE_EXTINGUISH)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH)
    )

    val MOLTEN_FLUID = FluidType(
        Properties.create()
            .pathType(PathType.LAVA)
            .density(5000)
            .lightLevel(10)
            .viscosity(8000)
            .temperature(1300)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
    )*/

    init {
        /*FLUID_TYPES.register("molten") { -> MOLTEN_FLUID }
        FLUIDS.register("gas") { -> GAS_FLUID }
        FLUIDS.register("hot_gas") { -> HOT_GAS_FLUID }
        FLUIDS.register("steam") { -> STEAM }*/
    }

    @SubscribeEvent
    fun registerClientExtensionsEvent(event: RegisterClientExtensionsEvent) {
        ModResources.RESOURCES.forEach { (_: String, resource: WorldResource) ->
            if (!resource.addFluid) return@forEach

            event.registerFluidType(object : IClientFluidTypeExtensions {
                private val STILL_TEXTURE: ResourceLocation =
                    ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still")
                    private val FLOWING_TEXTURE: ResourceLocation =
                        ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_flow")

                override fun getTintColor(): Int {
                    return resource.fluidColor
                }

                override fun getStillTexture(): ResourceLocation {
                    return STILL_TEXTURE
                }

                override fun getFlowingTexture(): ResourceLocation {
                    return FLOWING_TEXTURE
                }
            }, resource.fluidType)
        }
        /*event.registerFluidType(IClientFluidTypeExtensions.of(ModFluids.STEAM_FLUID), ModFluids.STEAM_FLUID)
        event.registerFluidType(IClientFluidTypeExtensions.of(ModFluids.MOLTEN_FLUID), ModFluids.MOLTEN_FLUID)
        event.registerFluidType(IClientFluidTypeExtensions.of(ModFluids.HOT_GAS_FLUID), ModFluids.HOT_GAS_FLUID)*/

        event.registerFluidType(object : IClientFluidTypeExtensions {
            private val STILL_TEXTURE: ResourceLocation =
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/water_still")

            override fun getTintColor(): Int {
                return -0xa0a0b
            }

            override fun getStillTexture(): ResourceLocation {
                return STILL_TEXTURE
            }

            override fun getFlowingTexture(): ResourceLocation {
                return STILL_TEXTURE
            }
        }, STEAM_TYPE)
    }
}