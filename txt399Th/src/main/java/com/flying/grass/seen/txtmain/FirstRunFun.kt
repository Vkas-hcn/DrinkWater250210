package com.flying.grass.seen.txtmain

import android.app.Application
import android.util.Log
import com.flying.grass.seen.adtool.ShowDataTool
import com.flying.grass.seen.adtool.TranplusConfig
import com.flying.grass.seen.adtool.TranplusUtils
import com.flying.grass.seen.limt.H5Limiter
import com.flying.grass.seen.txtdata.DrinkStartApp
import com.flying.grass.seen.txtdata.LocalStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

object FirstRunFun {
    lateinit var localStorage: LocalStorage
    lateinit var mainStart: Application
    var isVps: Boolean = true
    val adShowFun = TranplusUtils()
    val h5Limiter = H5Limiter()
    fun init(application: Application, mustXSData: Boolean) {
        if (!DrinkStartApp.isMainProcess(application)) {
            return
        }
        ShowDataTool.showLog("San MainStart init")
        localStorage = LocalStorage(application)
        mainStart = application
        isVps = mustXSData
        val path = "${mainStart.applicationContext.dataDir.path}/txtflag"
        File(path).mkdirs()
        ShowDataTool.showLog(" 文件名=: $path")
        val lifecycleObserver = IntetNetSHow()
        application.registerActivityLifecycleCallbacks(lifecycleObserver)
        SDKManagerFun.initialize(application)
        SDKManagerFun.getAndroidId()
        DrinkStartApp.startService()
        TranplusConfig.noShowICCC()
        RefDataFun.launchRefData()
        SDKManagerFun.sessionUp()
        SDKManagerFun.initAppsFlyer()
        SDKManagerFun.getFcmFun()
    }

    fun canIntNextFun() {
        adShowFun.startRomFun()
    }

    fun setFirstGo() {
        //协程延迟
        CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            val isFirstInApp = localStorage.isFirstInApp
            if (!isFirstInApp) {
                localStorage.isFirstInApp = true
                Log.e("TAG", "首次拉活自动关闭")
                android.os.Process.killProcess(android.os.Process.myPid())
            }
        }
    }


}