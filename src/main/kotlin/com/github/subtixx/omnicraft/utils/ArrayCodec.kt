package com.github.subtixx.omnicraft.utils

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import java.util.function.IntFunction

class ArrayCodec<A>(
    elementCodec: Codec<A>,
    private val arrayGenerator: IntFunction<Array<A>>
) : Codec<Array<A>> {
    private val listCodec: Codec<List<A>> = elementCodec.listOf()

    override fun <T> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Array<A>, T>> {
        return listCodec.decode(ops, input).map { res ->
            Pair.of(
                arrayGenerator.apply(res.first.size),
                res.second
            )
        }
    }

    override fun <T> encode(input: Array<A>, ops: DynamicOps<T>, prefix: T): DataResult<T> {
        return listCodec.encode(listOf(*input), ops, prefix)
    }
}