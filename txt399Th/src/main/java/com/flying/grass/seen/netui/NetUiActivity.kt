package com.flying.grass.seen.netui

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.flying.grass.seen.R
import com.flying.grass.seen.netool.TtPoint
import com.flying.grass.seen.txtmain.FirstRunFun
import com.flying.grass.seen.adtool.ShowDataTool
import com.flying.grass.seen.txtbean.TxtDataBean
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NetUiActivity : AppCompatActivity() {

    private lateinit var webViewInstance: WebView
    private lateinit var webLoadingLayout: LinearLayout
    private lateinit var webSettingsInstance: WebSettings
    private lateinit var closeButton: ImageView
    private var adminData: TxtDataBean? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeActivity()
        setupWebView()
        setupCloseButton()
        FirstRunFun.h5Limiter.recordShow()
        loadH5Data()
    }

    private fun initializeActivity() {
        setContentView(R.layout.activity_net)
        webViewInstance = findViewById(R.id.web_net)
        webLoadingLayout = findViewById(R.id.ll_loading)
        webSettingsInstance = webViewInstance.settings
        closeButton = findViewById(R.id.iv_close)
    }

    private fun setupWebView() {
        val data = ShowDataTool.getAdminData() ?: run {
            ShowDataTool.showLog("Admin data is null")
            return
        }
        adminData = data
        configureWebSettings()
        setWebViewClient()
    }

    private fun configureWebSettings() {
        webSettingsInstance.run {
            userAgentString = userAgentString.plus("/${adminData?.networkRules?.packageIdentifier?: ""}")
            javaScriptEnabled = true
        }
    }

    private fun setWebViewClient() {
        webViewInstance.webViewClient = WebViewClient()
    }

    private fun setupCloseButton() {
        closeButton.setOnClickListener {
            handleCloseButtonClick()
        }
    }

    private fun handleCloseButtonClick() {
        finish()
        TtPoint.postPointData(false, "closebrowser")
    }

    private fun loadH5Data() {

        try {
            val parsedData = adminData?.networkRules?.adUrls?.externalApp?:""
            ShowDataTool.showLog("load-h5-Url: $parsedData")
            loadWebViewContent(parsedData)
        } catch (e: Exception) {
            ShowDataTool.showLog("parseGateways error: ${e.message}")
        }
    }


    private fun loadWebViewContent(rul:String) {
        lifecycleScope.launch {
            webViewInstance.loadUrl(rul)
            delay(3000)
            hideLoadingLayout()
        }
    }

    private fun hideLoadingLayout() {
        webLoadingLayout.visibility = LinearLayout.GONE
    }
}