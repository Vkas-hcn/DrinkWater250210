package com.flying.grass.seen.txtdata

import android.content.Context
import android.content.pm.PackageManager

object InstallationInfo {
    fun getInstallTime(context: Context): Long {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.firstInstallTime
        } catch (e: PackageManager.NameNotFoundException) {
            0L
        }
    }

    fun getInstallDurationSeconds(context: Context): Long {
        return (System.currentTimeMillis() - getInstallTime(context)) / 1000
    }
}
