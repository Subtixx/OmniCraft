@file:Suppress("unused")

package com.github.subtixx.omnicraft.utils

import java.util.*
import kotlin.math.ln
import kotlin.math.pow

object NumberUtil {
    /**
     * Format number to K, M, G, T, P, E, Z, Y
     *
     * @param number
     * @return
     */
    @JvmStatic
    fun formatNumber(number: Int): String {
        if (number < 1000) return number.toString()

        var num = number.toDouble()
        var index = 0
        for (i in 0..7) {
            if (num < 1000) break
            num /= 1000
            index = i + 1
        }
        return String.format(Locale.ENGLISH, "%.2f%s", num, "KMGTPEZY"[index])
    }

    /**
     * Returns a human readable string of the bytes passed in e.g. 1024 kB
     *
     * @param bytes amount of bytes
     * @param si true if using SI units
     * @return human readable string
     */
    @JvmStatic
    fun humanReadableByteCount(bytes: Long, si: Boolean = false): String {
        val units = if (si) 1000 else 1024
        if (bytes < units) return "$bytes B"
        val exp = (ln(bytes.toDouble()) / ln(units.toDouble())).toInt()
        val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
        return String.format("%.1f %sB", bytes / units.toDouble().pow(exp.toDouble()), pre)
    }

    /**
     * Returns a human readable string of the time passed in seconds e.g. 2:00:00
     *
     * @param time
     * @return
     */
    @JvmStatic
    fun humanReadableTime(time: Long): String {
        val hours = time / 3600
        val minutes = (time - hours * 3600) / 60
        val seconds = (time - hours * 3600 - minutes * 60)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    /**
     * Returns a human readable string of the time passed in seconds e.g. 2 hours ago
     *
     * @param time the time in seconds
     * @return a human readable string
     */
    @JvmStatic
    fun humanReadableAgo(time: Long): String {
        val hours = time / 3600
        val minutes = (time - hours * 3600) / 60
        val days = hours / 24
        return if (days > 0) String.format("%d days ago", days)
        else if (hours > 0) String.format("%d hours ago", hours)
        else if (minutes > 0) String.format("%d minutes ago", minutes)
        else String.format("%d seconds ago", time)
    }
}