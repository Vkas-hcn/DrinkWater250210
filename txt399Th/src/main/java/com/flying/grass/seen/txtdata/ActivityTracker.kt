package com.flying.grass.seen.txtdata

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContextCompat
import com.flying.grass.seen.adtool.ShowDataTool
import com.flying.grass.seen.netool.TtPoint
import com.flying.grass.seen.txtdata.DrinkStartApp.isServiceRunning
import com.flying.grass.seen.txtmain.FirstRunFun.mainStart
import com.flying.grass.seen.uican.UiCanActivity
import com.flying.grass.seen.zztw.sercan.TxtNiFS

object ActivityTracker {
    private val activities = ArrayList<Activity>()

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activities.removeAll { it == activity }
    }

    fun closeAllActivities() {
        activities.forEach {
            it.finishAndRemoveTask()
        }
        activities.clear()
    }

    fun isHaveActivity(): Boolean {
        return activities.isNotEmpty()
    }
    fun getActivity(): ArrayList<Activity> {
        return activities
    }

    fun addFun(activity: Activity){
        addActivity(activity)
        if (!isServiceRunning) {
            ContextCompat.startForegroundService(
                mainStart,
                Intent( mainStart, TxtNiFS::class.java)
            )
        }
    }
    fun xiug(activity: Activity){
        if (activity is UiCanActivity) {
            return
        }
        if (activity.javaClass.name.contains(DrinkConfigData.startPack2)) {
            ShowDataTool.showLog("onActivityStarted=${activity.javaClass.name}")
            val anTime = DrinkStartApp.getInstallDuration()
            TtPoint.postPointData(false, "session_front", "time", anTime)
        }
    }
}
