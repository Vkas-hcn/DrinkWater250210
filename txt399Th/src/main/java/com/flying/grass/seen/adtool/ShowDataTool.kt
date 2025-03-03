package com.flying.grass.seen.adtool

import android.util.Log
import com.google.gson.Gson
import com.flying.grass.seen.txtbean.TxtDataBean
import com.flying.grass.seen.txtdata.DrinkConfigData
import com.flying.grass.seen.txtmain.FirstRunFun

object ShowDataTool {
    private const val LOG_TAG = "Drink"
    fun showLog(data: String) {
        if (!FirstRunFun.isVps) {
            Log.e(LOG_TAG, data)
        }
    }

    fun getAdminData(): TxtDataBean? {
//        FirstRunFun.localStorage.admindata = DrinkConfigData.data_can
        val adminData = FirstRunFun.localStorage.admindata
        return try {
            val adminBean = Gson().fromJson(adminData, TxtDataBean::class.java)
            if (adminBean != null && isValidAdminBean(adminBean)) adminBean else null
        } catch (e: Exception) {
            showLog("Failed to parse admin data: ${e.message}")
            null
        }
    }

    /**
     * 验证管理员数据的有效性。
     */
    private fun isValidAdminBean(bean: TxtDataBean): Boolean {
        return bean.accountProfile?.type != null &&
                bean.promotionConfig != null &&
                bean.creativeResources.promoId.isNotEmpty()
    }


    fun putAdminData(adminBean: String) {
        FirstRunFun.localStorage.admindata = adminBean
//        FirstRunFun.localStorage.admindata = DrinkConfigData.data_can
    }
}