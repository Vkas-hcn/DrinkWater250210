package com.flying.grass.seen.txtmain

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.flying.grass.seen.adtool.ShowDataTool
import com.flying.grass.seen.netool.AppPointData
import com.flying.grass.seen.netool.TtPoint
import com.flying.grass.seen.txtdata.DrinkConfigData
import com.flying.grass.seen.txtmain.FirstRunFun.localStorage
import com.flying.grass.seen.txtmain.FirstRunFun.mainStart
import com.flying.grass.seen.txtmain.FirstRunFun.isVps
import com.flying.grass.seen.zload.NtLoad
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.tradplus.ads.open.TradPlusSdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

object SDKManagerFun {
    fun initialize(context: Context) {
        TradPlusSdk.setTradPlusInitListener {
        }
        TradPlusSdk.initSdk(mainStart, DrinkConfigData.getConfig().appid)
//        NtLoad.txtTheme(context)
        CanFileGo.loadEncryptedSo(context)
    }
    @SuppressLint("HardwareIds")
     fun getAndroidId() {
        val adminData = localStorage.appiddata
        if (adminData.isEmpty()) {
            val androidId =
                Settings.Secure.getString(mainStart.contentResolver, Settings.Secure.ANDROID_ID)
            if (!androidId.isNullOrBlank()) {
                localStorage.appiddata = androidId
            } else {
                localStorage.appiddata = UUID.randomUUID().toString()
            }
        }
    }
    fun sessionUp() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                TtPoint.postPointData(false, "session_up")
                delay(1000 * 60 * 15)
            }
        }
    }

    fun initAppsFlyer() {
        val appsFlyer = AppsFlyerLib.getInstance()
        val config = DrinkConfigData.getConfig()

        ShowDataTool.showLog("AppsFlyer-id: $${config.appsflyId}")

        appsFlyer.init(config.appsflyId, object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversionDataMap: MutableMap<String, Any>?) {
                // 安全类型转换+默认值处理
                val status = (conversionDataMap?.get("af_status") as? String) ?: "no_data"
                ShowDataTool.showLog("AppsFlyer: $status")
                AppPointData.pointInstallAf(status)
                    conversionDataMap?.forEach { (key, value) ->
                        ShowDataTool.showLog("AppsFlyer-all: key=$key | value=${value.toString().take(200)}")
                    }
            }

            override fun onConversionDataFail(p0: String?) {
                ShowDataTool.showLog("AppsFlyer: onConversionDataFail: $p0")
            }

            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                ShowDataTool.showLog("AppsFlyer: onAppOpenAttribution: ${p0?.toLogFormat()}")
            }

            override fun onAttributionFailure(p0: String?) {
                ShowDataTool.showLog("AppsFlyer: onAttributionFailure: $p0")
            }

        }, mainStart)

        with(appsFlyer) {
            setCustomerUserId(localStorage.appiddata)
            start(mainStart)
            logEvent(mainStart, "darink_422_install", hashMapOf<String, Any>().apply {
                put("customer_user_id", localStorage.appiddata)
                put("app_version", AppPointData.showAppVersion())
                put("os_version", Build.VERSION.RELEASE)
                put("bundle_id", mainStart.packageName)
                put("language", "asc_wds")
                put("platform", "raincoat")
                put("android_id", localStorage.appiddata)
            })
        }
    }

    private fun Map<*, *>.toLogFormat(): String = entries.joinToString(" | ") {
        "${it.key}=${it.value.toString().take(50)}"
    }

    fun getFcmFun() {
        if (!isVps) return
        if (localStorage.fcmState) return
        runCatching {
            Firebase.messaging.subscribeToTopic(DrinkConfigData.fffmmm)
                .addOnSuccessListener {
                    localStorage.fcmState = true
                    ShowDataTool.showLog("Firebase: subscribe success")
                }
                .addOnFailureListener {
                    ShowDataTool.showLog("Firebase: subscribe fail")
                }
        }
    }
}