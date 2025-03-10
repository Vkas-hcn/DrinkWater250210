package com.flying.grass.seen.uican

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.flying.grass.seen.txtdata.DrinkStartApp
import com.flying.grass.seen.netool.TtPoint
import com.flying.grass.seen.txtmain.FirstRunFun
import com.flying.grass.seen.txtmain.FirstRunFun.adShowFun
import com.flying.grass.seen.adtool.ShowDataTool
import com.flying.grass.seen.adtool.TranplusConfig
import com.flying.grass.seen.netool.AppPointData.firstExternalBombPoint
import com.flying.grass.seen.netui.NetUiActivity
import com.flying.grass.seen.zload.NtLoad
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class UiCanActivity : AppCompatActivity() {

    private lateinit var activityContext: Context
    private var adDelayDuration: Long = 0L
    private var isAdReady: Boolean = false
    private var isH5State: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityContext = this
        log("外弹页面展示")
        initializeActivity()
    }

    private fun initializeActivity() {
        NtLoad.abbrevArmy(activityContext)
        FirstRunFun.localStorage.isAdFailCount = 0
        isH5State = DrinkStartApp.isH5State
        if (isH5State) {
            navigateToH5Activity()
        } else {
            handleNonH5Content()
        }
    }

    private fun navigateToH5Activity() {
        startActivity(Intent(activityContext, NetUiActivity::class.java))
        TtPoint.postPointData(false, "browserjump")
    }

    private fun handleNonH5Content() {
        adDelayDuration = generateRandomDelay()
        firstExternalBombPoint()
        TtPoint.postPointData(false, "starup", "time", adDelayDuration / 1000)
        // 检查广告是否准备好
        isAdReady = adShowFun.interstitialAd.isReady
        if (isAdReady) {
            handleAdReadyState()
        } else {
            finish()
        }
    }

    private fun handleAdReadyState() {
        log("Advertisement display delay duration: $adDelayDuration")
        TtPoint.postPointData(false, "isready")
        lifecycleScope.launch {
            delay(adDelayDuration)
            TtPoint.postPointData(false, "delaytime", "time", adDelayDuration / 1000)
            setAdShowTimes()
            adShowFun.interstitialAd.showAd(this@UiCanActivity,"ces")
            delay(30000)
            if (TranplusConfig.showAdTime == 0L) {
                TtPoint.postPointData(false, "show", "t", "30")
                TranplusConfig.showAdTime = 0
            }
        }
    }

    private fun setAdShowTimes() {
        val currentTime = System.currentTimeMillis()
        TranplusConfig.showAdTime = currentTime
        TranplusConfig.adShowTime = currentTime
    }

    private fun generateRandomDelay(): Long {
        val adminData = ShowDataTool.getAdminData()
        val delayRange = adminData?.timingParameters?.delayRange
        val minDelay = delayRange?.minDelayMs?.toLong() ?: 0L
        val maxDelay = delayRange?.maxDelayMs?.toLong() ?: 0L
        return Random.nextLong(minDelay, maxDelay + 1)
    }

    override fun onDestroy() {
        (window.decorView as ViewGroup).removeAllViews()
        super.onDestroy()
    }

    private fun log(message: String) {
        ShowDataTool.showLog(message)
    }

}