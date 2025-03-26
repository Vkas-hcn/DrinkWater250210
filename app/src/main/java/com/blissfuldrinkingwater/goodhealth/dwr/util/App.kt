package com.blissfuldrinkingwater.goodhealth.dwr.util

import android.app.Application
import com.flying.grass.seen.txtmain.FirstRunFun

class App: Application() {

    companion object {
        lateinit var app:App
    }

    override fun onCreate() {
        super.onCreate()
        app = this;
        Kv.init(this)
        FirstRunFun.init(this,false)
    }
}