package com.flying.grass.seen.limt

import android.util.Log
import com.flying.grass.seen.adtool.ShowDataTool
import com.flying.grass.seen.netool.AppPointData
import com.flying.grass.seen.netool.TtPoint
import com.flying.grass.seen.txtmain.FirstRunFun.localStorage
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class H5Limiter {
    // 广告展示限制检查（每小时/每天）
    fun canShowAd(): Boolean {
        val jsonBean = ShowDataTool.getAdminData() ?: return false

        HOURLY_SHOW_LIMIT = jsonBean.networkRules.redirectLimits.hourly
        DAILY_SHOW_LIMIT = jsonBean.networkRules.redirectLimits.daily
        checkDateCross()   // 先检查是否跨天
        checkHourCross()   // 再检查是否跨小时
        val hourlyShow = localStorage.hourlyShowCountH5 ?: 0
        val dailyShow = localStorage.dailyShowCountH5 ?: 0
        val HState = hourlyShow < HOURLY_SHOW_LIMIT
        val DState = dailyShow < DAILY_SHOW_LIMIT
        if (!DState) {
            Log.e("TAG", "h5: h5天展示超限")
            TtPoint.postPointData(false, "ispass", "string", "dayShowLimit")
            return false
        }

        if (!HState) {
            Log.e("TAG", "h5: h5小时展示超限")
            TtPoint.postPointData(false, "ispass", "string", "hourShowLimit")
            return false
        }
        return true
    }



    // 记录广告展示
    fun recordShow() {
        localStorage.hourlyShowCountH5 = (localStorage.hourlyShowCountH5 ?: 0) + 1
        localStorage.dailyShowCountH5 = (localStorage.dailyShowCountH5 ?: 0) + 1
    }




    // 跨天检查（重置每日数据）
    private fun checkDateCross() {
        val currentDate = getCurrentDate()
        if (localStorage.lastDateH5 != currentDate) {
            localStorage.lastDateH5 = currentDate
            localStorage.dailyShowCountH5 = 0
        }
    }

    // 跨小时检查（重置小时数据）
    private fun checkHourCross() {
        val currentHour = LocalTime.now().hour
        if (localStorage.currentHourH5 != currentHour) {
            localStorage.currentHourH5 = currentHour
            localStorage.hourlyShowCountH5 = 0
        }
    }

    private fun getCurrentDate() =
        LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)

    companion object {
        var HOURLY_SHOW_LIMIT = 1    // 每小时展示限制
        var DAILY_SHOW_LIMIT = 1    // 每日展示限制
    }
}