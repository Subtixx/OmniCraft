package com.github.subtixx.omnicraft.light

import java.util.*


object LightSerializerRegistry {
    private val registry = HashMap<String, ILightSerializer>()

    init {
        registerLightSerializer(MegaTorchSerializer.INSTANCE)
    }

    private fun registerLightSerializer(serializer: ILightSerializer) {
        val lightSerializerKey = serializer.serializerKey
        if (registry.containsKey(lightSerializerKey)) throw RuntimeException("lightSerializer '$lightSerializerKey' already exists")

        registry[lightSerializerKey] = serializer
    }

    fun getLightSerializer(lightSerializerKey: String): Optional<ILightSerializer> {
        if (!registry.containsKey(lightSerializerKey)) return Optional.empty()

        return Optional.of(registry[lightSerializerKey]!!)
    }
}
