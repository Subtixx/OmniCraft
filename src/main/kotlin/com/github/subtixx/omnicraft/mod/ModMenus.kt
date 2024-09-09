package com.github.subtixx.omnicraft.mod

import com.github.subtixx.omnicraft.Omnicraft
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension
import net.neoforged.neoforge.network.IContainerFactory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModMenus {
    val REGISTRY: DeferredRegister<MenuType<*>> = DeferredRegister.create(BuiltInRegistries.MENU, Omnicraft.ID)

    private fun <T : AbstractContainerMenu?> registerMenuType(
        name: String,
        factory: IContainerFactory<T>
    ): DeferredHolder<MenuType<*>, MenuType<T>> {
        return REGISTRY.register(name) { -> IMenuTypeExtension.create(factory) }
    }

    @SubscribeEvent
    fun register(event: RegisterMenuScreensEvent) {

    }
}