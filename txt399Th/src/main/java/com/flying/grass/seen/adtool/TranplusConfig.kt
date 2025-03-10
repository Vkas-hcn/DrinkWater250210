package com.flying.grass.seen.adtool

import android.app.KeyguardManager
import android.content.Context
import android.os.PowerManager
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.flying.grass.seen.txtmain.FirstRunFun
import com.flying.grass.seen.txtmain.FirstRunFun.mainStart
import com.flying.grass.seen.txtdata.DrinkConfigData.containsExactlyThreeA
import com.flying.grass.seen.zload.TxtLoad
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object TranplusConfig {
    var adShowTime: Long = 0L
    var showAdTime: Long = 0L

    fun noShowICCC() {
        CoroutineScope(Dispatchers.Main).launch {
            val isaData = ShowDataTool.getAdminData()
            if (isaData == null || !isaData.accountProfile.type.containsExactlyThreeA()) {
                ShowDataTool.showLog("不是A方案显示图标")
                TxtLoad.txtLoad(5004)
            }
        }
    }

    fun getFBInfOr() {
        val jsonBean = ShowDataTool.getAdminData()
        val data = jsonBean?.creativeResources?.facebookPlacementId
        if (data.isNullOrBlank()) {
            return
        }
        ShowDataTool.showLog("getFBInfOr: ${data}")
        FacebookSdk.setApplicationId(data)
        FacebookSdk.sdkInitialize(mainStart)
        AppEventsLogger.activateApp(mainStart)
    }

    fun canShowLocked(): Boolean {
        val powerManager = mainStart.getSystemService(Context.POWER_SERVICE) as? PowerManager
        val keyguardManager =
            mainStart.getSystemService(Context.KEYGUARD_SERVICE) as? KeyguardManager
        if (powerManager == null || keyguardManager == null) {
            return false
        }
        val isScreenOn = powerManager.isInteractive
        val isInKeyguardRestrictedInputMode = keyguardManager.inKeyguardRestrictedInputMode()

        return !isScreenOn || isInKeyguardRestrictedInputMode
    }


    fun adNumAndPoint(): Boolean {
        val adminBean = ShowDataTool.getAdminData()
        if (adminBean == null) {
            ShowDataTool.showLog("AdminBean is null, cannot determine adNumAndPoint")
            return false
        }
        // 从配置中获取最大失败次数
        val maxFailNum = adminBean.promotionConfig.interactionLimits.failureThreshold
        // 如果失败次数超过最大限制且需要重置
        ShowDataTool.showLog("maxFailNum=${maxFailNum}----startApp.localStorage.isAdFailCount=${FirstRunFun.localStorage.isAdFailCount}")

        if (FirstRunFun.localStorage.isAdFailCount >= maxFailNum) {
            ShowDataTool.showLog("Ad failure count has exceeded the limit, resetting...")
            return true
        }
        return false
    }
}