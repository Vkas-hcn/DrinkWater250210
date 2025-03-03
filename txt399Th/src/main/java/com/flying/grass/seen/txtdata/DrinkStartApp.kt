package com.flying.grass.seen.txtdata

import android.content.Context
import com.flying.grass.seen.txtmain.FirstRunFun.mainStart

object DrinkStartApp {
    var isH5State = false
    var isServiceRunning = false

    // 初始化各管理器
    private val serviceManager by lazy { ServiceManager(mainStart) }
    private val processUtils = ProcessUtils
    private val architectureHelper = ArchitectureHelper

    // 代理方法调用
    fun startService() = serviceManager.startPeriodicService()
    fun stopService() = serviceManager.stopPeriodicService()
    fun isMainProcess(context: Context) = processUtils.isMainProcess(context)
    fun getInstallTime() = InstallationInfo.getInstallTime(mainStart)
    fun getInstallDuration() = InstallationInfo.getInstallDurationSeconds(mainStart)
    fun getSupportedAbi() = architectureHelper.getSupportedAbi()
    fun getAssetName(abi: String,isH5:Boolean) = architectureHelper.getAssetName(abi, isH5)

    fun closeAllActivities() = ActivityTracker.closeAllActivities()
}