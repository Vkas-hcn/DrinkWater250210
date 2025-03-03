package com.flying.grass.seen.txtmain


import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.annotation.Keep
import com.flying.grass.seen.txtdata.ActivityTracker

@Keep
class IntetNetSHow : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityTracker.addFun(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        ActivityTracker.xiug(activity)
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityTracker.removeActivity(activity)
    }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
    }
}
