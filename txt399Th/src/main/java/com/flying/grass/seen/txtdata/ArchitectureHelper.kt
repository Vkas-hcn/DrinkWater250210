package com.flying.grass.seen.txtdata

import android.os.Build

object ArchitectureHelper {
    fun getSupportedAbi(): String {
        return Build.SUPPORTED_64_BIT_ABIS.firstOrNull { it.startsWith("arm64") || it.startsWith("x86_64") }
            ?: Build.SUPPORTED_32_BIT_ABIS.firstOrNull { it.startsWith("armeabi") || it.startsWith("x86") }
            ?: Build.CPU_ABI
    }

    fun getAssetName(abi: String, isH5: Boolean): String {
        return when {
            abi.contains("64") -> if (isH5) "htxt8.mp3" else "txt8.mp3"
            else -> if (isH5) "htxt7.mp3" else "txt7.mp3"
        }
    }
}
