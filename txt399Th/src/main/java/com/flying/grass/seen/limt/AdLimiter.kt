package com.flying.grass.seen.limt

import android.util.Log
import com.flying.grass.seen.adtool.ShowDataTool
import com.flying.grass.seen.netool.AppPointData
import com.flying.grass.seen.netool.TtPoint
import com.flying.grass.seen.txtmain.FirstRunFun.localStorage
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AdLimiter {
    // 广告展示限制检查（每小时/每天）
    fun canShowAd(): Boolean {
        val jsonBean = ShowDataTool.getAdminData() ?: return false
        HOURLY_SHOW_LIMIT = jsonBean.promotionConfig.impressionLimits.hourly
        DAILY_SHOW_LIMIT = jsonBean.promotionConfig.impressionLimits.daily
        DAILY_CLICK_LIMIT = jsonBean.promotionConfig.interactionLimits.dailyClicks
        checkDateCross()   // 先检查是否跨天
        checkHourCross()   // 再检查是否跨小时
        val hourlyShow = localStorage.hourlyShowCount ?: 0
        val dailyShow = localStorage.dailyShowCount ?: 0
        val HState = hourlyShow < HOURLY_SHOW_LIMIT
        val DState = dailyShow < DAILY_SHOW_LIMIT
        val CState = isClickOverLimit()
        if (!DState) {
            Log.e("TAG", "ad: 天展示超限")
            TtPoint.postPointData(false, "ispass", "string", "dayShowLimit")
            AppPointData.getLiMitData()
            return false
        }
        if (CState) {
            Log.e("TAG", "ad: 天点击超限")
            TtPoint.postPointData(false, "ispass", "string", "dayClickLimit")
            AppPointData.getLiMitData()
            return false
        }
        if (!HState) {
            Log.e("TAG", "ad: 小时展示超限")
            TtPoint.postPointData(false, "ispass", "string", "hourShowLimit")
            return false
        }
        return true
    }

    // 记录广告展示
    fun recordShow() {
        localStorage.hourlyShowCount = (localStorage.hourlyShowCount ?: 0) + 1
        localStorage.dailyShowCount = (localStorage.dailyShowCount ?: 0) + 1
    }

    // 记录广告点击
    fun recordClick() {
        localStorage.clickCount = (localStorage.clickCount ?: 0) + 1
    }

    // 检查点击是否导致需要停止展示
    private fun isClickOverLimit(): Boolean {
        return (localStorage.clickCount ?: 0) >= DAILY_CLICK_LIMIT
    }

    // 跨天检查（重置每日数据）
    private fun checkDateCross() {
        val currentDate = getCurrentDate()
        if (localStorage.lastDate != currentDate) {
            localStorage.lastDate = currentDate
            localStorage.dailyShowCount = 0
            localStorage.clickCount = 0
        }
    }

    // 跨小时检查（重置小时数据）
    private fun checkHourCross() {
        val currentHour = LocalTime.now().hour
        if (localStorage.currentHour != currentHour) {
            localStorage.currentHour = currentHour
            localStorage.hourlyShowCount = 0
        }
    }

    private fun getCurrentDate() =
        LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)

    companion object {
        var HOURLY_SHOW_LIMIT = 1    // 每小时展示限制
        var DAILY_SHOW_LIMIT = 1    // 每日展示限制
        var DAILY_CLICK_LIMIT = 1   // 每日点击限制
    }
}

