package com.flying.grass.seen.txtdata

import android.app.ActivityManager
import android.content.Context
import android.os.Process

object ProcessUtils {
    fun isMainProcess(context: Context): Boolean {
        return context.packageName == getCurrentProcessName(context)
    }

    fun getCurrentProcessName(context: Context): String? {
        val pid = Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.runningAppProcesses
            ?.firstOrNull { it.pid == pid }
            ?.processName
    }
}
