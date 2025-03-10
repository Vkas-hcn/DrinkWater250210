package com.flying.grass.seen.netool


import android.annotation.SuppressLint
import android.util.Base64
import com.flying.grass.seen.txtmain.FirstRunFun
import com.flying.grass.seen.adtool.ShowDataTool
import com.google.gson.Gson
import com.flying.grass.seen.txtbean.TxtDataBean
import com.flying.grass.seen.txtdata.DrinkConfigData
import com.flying.grass.seen.txtdata.DrinkConfigData.containsExactlyThreeA
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

import java.nio.charset.StandardCharsets

import java.net.SocketTimeoutException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object NetTool {
    interface ResultCallback {
        fun onComplete(result: String)
        fun onError(message: String)
    }

    private val threadPool = Executors.newFixedThreadPool(4)
    private val okHttpClient by lazy {
        OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }
    fun executeAdminRequest(callback: ResultCallback) {
        val requestData = prepareRequestData()
        ShowDataTool.showLog("executeAdminRequest=${DrinkConfigData.getConfig().adminUrl}")
        TtPoint.postPointData(false, "reqadmin")

        threadPool.execute {
            try {
                val (processedData, datetime) = processRequestData(requestData)

                val request = Request.Builder()
                    .url(DrinkConfigData.getConfig().adminUrl)
                    .post(processedData.toRequestBody("application/json".toMediaType()))
                    .addHeader("datetime", datetime)
                    .build()

                okHttpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        callback.onError("HTTP error: ${response.code}")
                        AppPointData.getadmin(false, response.code.toString())
                        return@use
                    }

                    handleOkHttpAdminResponse(response, callback)
                }
            } catch (e: SocketTimeoutException) {
                callback.onError("Request timed out: ${e.message}")
                AppPointData.getadmin(false, "timeout")
            } catch (e: Exception) {
                callback.onError("Operation failed: ${e.message}")
                AppPointData.getadmin(false, "timeout")
            }
        }
    }

    // 适配后的响应处理方法
    private fun handleOkHttpAdminResponse(response: Response, callback: ResultCallback) {
        try {
            val datetimeHeader = response.header("datetime")
                ?: throw IllegalArgumentException("Missing datetime header")

            response.body?.string()?.let { responseString ->
                // 解密处理
                val decodedBytes = Base64.decode(responseString, Base64.DEFAULT)
                val decodedStr = String(decodedBytes, StandardCharsets.UTF_8)
                val finalData = xorEncrypt(decodedStr, datetimeHeader)

                // 解析数据
                val jsonResponse = JSONObject(finalData)
                val jsonData = parseAdminRefData(jsonResponse.toString())
                val adminBean = runCatching {
                    Gson().fromJson(jsonData, TxtDataBean::class.java)
                }.getOrNull()

                when {
                    adminBean == null -> {
                        callback.onError("Invalid response format")
                        ShowDataTool.showLog("请求结果=$jsonData")
                        AppPointData.getadmin(false, null)
                    }
                    ShowDataTool.getAdminData() == null -> {
                        ShowDataTool.putAdminData(jsonData)
                        val type = adminBean.accountProfile.type.containsExactlyThreeA()
                        AppPointData.getadmin(type, response.code.toString())
                        callback.onComplete(jsonData)
                    }
                    adminBean.accountProfile.type.containsExactlyThreeA() -> {
                        ShowDataTool.putAdminData(jsonData)
                        AppPointData.getadmin(true, response.code.toString())
                        callback.onComplete(jsonData)
                    }
                    else -> {
                        AppPointData.getadmin(false, response.code.toString())
                        callback.onComplete(jsonData)
                    }
                }
            } ?: run {
                callback.onError("Empty response body")
                AppPointData.getadmin(false, "empty_body")
            }
        } catch (e: Exception) {
            callback.onError("Response processing failed: ${e.message}")
            AppPointData.getadmin(false, "parse_error")
        }
    }

    // 改造后的executePutRequest
    fun executePutRequest(body: Any, callback: ResultCallback) {
        threadPool.execute {
            try {
                val jsonBody = JSONObject(body.toString()).toString()
                val request = Request.Builder()
                    .url(DrinkConfigData.getConfig().upUrl)
                    .post(jsonBody.toRequestBody("application/json".toMediaType()))
                    .build()

                okHttpClient.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        callback.onError("HTTP error: ${response.code}")
                        return@use
                    }else{
                        callback.onComplete(response.body.toString())
                    }
                }
            } catch (e: Exception) {
                callback.onError("Put request failed: ${e.message}")
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun prepareRequestData(): String {
        return JSONObject().apply {
            put("zDAIGEAbLI", "com.water.note.mate.fresh.leaf")
            put("lpOMVKK", FirstRunFun.localStorage.appiddata)
            put("uXyEpJnj", FirstRunFun.localStorage.refdata)
//            put("uXyEpJnj", "organic111")
            put("ztjxIrQALy", AppPointData.showAppVersion())
        }.toString()
    }


    private fun processRequestData(rawData: String): Pair<String, String> {
        val datetime = System.currentTimeMillis().toString()
        val encrypted = xorEncrypt(rawData, datetime)
        return Base64.encodeToString(encrypted.toByteArray(), Base64.NO_WRAP) to datetime
    }

    private fun xorEncrypt(text: String, datetime: String): String {
        val cycleKey = datetime.toCharArray()
        return text.mapIndexed { index, char ->
            char.toInt().xor(cycleKey[index % cycleKey.size].toInt()).toChar()
        }.joinToString("")
    }

    private fun parseAdminRefData(jsonString: String): String {
        return try {
            JSONObject(jsonString).getJSONObject("KOAtQ").getString("conf")
        } catch (e: Exception) {
            ""
        }
    }

}



