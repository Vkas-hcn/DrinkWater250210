package com.blissfuldrinkingwater.goodhealth.dwr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blissfuldrinkingwater.goodhealth.dwr.databinding.ActivityMainBinding
import com.blissfuldrinkingwater.goodhealth.dwr.util.Kv
import com.flying.grass.seen.txtdata.DrinkConfigData
import com.tradplus.ads.base.bean.TPAdError
import com.tradplus.ads.base.bean.TPAdInfo
import com.tradplus.ads.base.bean.TPBaseAd
import com.tradplus.ads.open.splash.SplashAdListener
import com.tradplus.ads.open.splash.TPSplash
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MainActivityFFF : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var showJob: Job? = null
    private var mTPSplash: TPSplash? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Kv.initTargetValue()
        initAd()
    }

    override fun onResume() {
        super.onResume()
        showOpenAd()
    }

    override fun onPause() {
        stop()
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    private fun stop() {
        showJob?.cancel()
    }

    // 初始化广告位
    private fun initAd() {
        if (mTPSplash == null) {
            mTPSplash = TPSplash(this, DrinkConfigData.getConfig().openid)
            mTPSplash!!.setAdListener(object : SplashAdListener() {
                override fun onAdLoaded(tpAdInfo: TPAdInfo, tpBaseAd: TPBaseAd?) {
                    Log.e("MainActivityFFF", "开屏广告加载成功")
                }

                override fun onAdLoadFailed(tpAdError: TPAdError) {
                    Log.e("MainActivityFFF", "开屏广告加载失败=${tpAdError.errorCode}--${tpAdError.errorMsg}")
                    navigateToMainActivity()
                }

                override fun onAdClosed(tpAdInfo: TPAdInfo) {
                    Log.e("MainActivityFFF", "开屏广告关闭")
                    binding.vrinhvsd.removeAllViews()
                    navigateToMainActivity()
                }
            })
        }
        mTPSplash?.loadAd(null)
    }

    private fun showOpenAd() {
        showJob?.cancel()
        showJob = lifecycleScope.launch {
            try {
                withTimeout(10000L) {
                    while (isActive && !mTPSplash?.isReady!!) {
                        delay(500L)
                    }
                    mTPSplash?.showAd(binding.vrinhvsd)
                }
            } catch (e: TimeoutCancellationException) {
                Log.e("MainActivityFFF", "广告显示超时", e)
                navigateToMainActivity()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this@MainActivityFFF, HomeActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}