package com.flying.grass.seen.netool

import android.util.Log
import com.flying.grass.seen.netool.AppPointData.upAdJson
import com.flying.grass.seen.netool.AppPointData.upInstallJson
import com.flying.grass.seen.netool.AppPointData.upPointJson
import com.flying.grass.seen.txtmain.FirstRunFun
import com.flying.grass.seen.txtmain.FirstRunFun.canIntNextFun
import com.flying.grass.seen.adtool.ShowDataTool
import com.flying.grass.seen.txtdata.DrinkConfigData.containsExactlyThreeA
import com.tradplus.ads.base.bean.TPAdInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resumeWithException
import kotlin.random.Random
import kotlin.coroutines.resume

object TtPoint {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    sealed class Result<out T> {
        data class Success<out T>(val value: T) : Result<T>()
        data class Failure(val exception: Throwable) : Result<Nothing>()
    }

    fun onePostAdmin() {
        scope.launch {
            ShowDataTool.showLog("无数据启动请求:")
            executeWithRetry(
                maxRetries = 3,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = NetTool.executeAdminRequestSuspend()
                    val bean = ShowDataTool.getAdminData()
                    ShowDataTool.showLog("admin-data-1=${bean?.accountProfile?.type?.containsExactlyThreeA()}=: $result")
                    if (bean != null && !bean.accountProfile.type.containsExactlyThreeA()) {
                        ShowDataTool.showLog("不是A用户，进行重试")
                        bPostAdmin()
                    }
                    if (bean?.accountProfile?.type?.containsExactlyThreeA() == true) {
                        canIntNextFun()
                    }
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(3, "Admin", e, attempt)
                    Result.Failure(e)
                }
            }
        }
    }
    fun twoPostAdmin() {
        ShowDataTool.showLog("有数据启动请求:")
        scope.launch {
            val bean = ShowDataTool.getAdminData()
            val delay = Random.nextLong(1000, 10 * 60 * 1000)
            var isStart = false

            if (bean != null && bean.accountProfile.type.containsExactlyThreeA()) {
                isStart = true
                canIntNextFun()
                ShowDataTool.showLog("冷启动app延迟 ${delay}ms 请求admin数据")
                delay(delay)
            }
            executeWithRetry(
                maxRetries = 3,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = NetTool.executeAdminRequestSuspend()
                    val beanDAta = ShowDataTool.getAdminData()
                    ShowDataTool.showLog("admin-data-2: $result")
                    if (beanDAta != null && !beanDAta.accountProfile.type.containsExactlyThreeA()) {
                        bPostAdmin()
                    }
                    if (beanDAta?.accountProfile?.type?.containsExactlyThreeA() == true && !isStart) {
                        canIntNextFun()
                    }
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(3, "Admin", e, attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    private fun bPostAdmin() {
        scope.launch {
            // 执行带重试机制的请求
            executeWithRetry(
                maxRetries = 6,
                minDelay = 59_000L,
                maxDelay = 60_000L
            ) { attempt ->
                try {
                    val result = NetTool.executeAdminRequestSuspend()
                    val bean = ShowDataTool.getAdminData()
                    ShowDataTool.showLog("admin-onSuccess: $result")
                    if (bean != null && !bean.accountProfile.type.containsExactlyThreeA()) {
                        handleError(10, "不是A用户，进行重试", Exception(), attempt)
                        Result.Failure(Exception())
                    } else {
                        canIntNextFun()
                        Result.Success(Unit)
                    }
                } catch (e: Exception) {
                    handleError(10, "Admin", e, attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    fun postInstallData() {
        scope.launch {
            val data = withContext(Dispatchers.Default) {
                FirstRunFun.localStorage.IS_INT_JSON.ifEmpty {
                    val newData = upInstallJson()
                    FirstRunFun.localStorage.IS_INT_JSON = newData
                    newData
                }
            }

            ShowDataTool.showLog("Install: data=$data")

            // 执行带重试机制的请求
            executeWithRetry(
                maxRetries = 20,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = NetTool.executePutRequestSuspend(data)
                    handleSuccess("Install", result)
                    FirstRunFun.localStorage.IS_INT_JSON = ""
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(20, "Install", e, attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    fun postAdData(adValue: TPAdInfo) {
        scope.launch {
            // 准备请求数据
            val data = upAdJson(adValue)
            ShowDataTool.showLog("体外广告上报: data=$data")
            // 执行带重试机制的请求
            executeWithRetry(
                maxRetries = 3,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = NetTool.executePutRequestSuspend(data)
                    handleSuccess("体外广告上报", result)
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(3, "体外广告上报", e, attempt)
                    Result.Failure(e)
                }
            }
            AppPointData.postAdValue(adValue)
        }
    }

    fun postPointData(
        isAdMinCon: Boolean,
        name: String,
        key1: String? = null,
        keyValue1: Any? = null,
        key2: String? = null,
        keyValue2: Any? = null
    ) {
        scope.launch {
            val adminBean = ShowDataTool.getAdminData()
            val state = adminBean?.accountProfile?.permissions?.uploadEnabled?:1
            Log.e("TAG", "postPointData: ${state}", )
            if (!isAdMinCon && state != 1) {
                return@launch
            }
            // 准备请求数据
            val data = if (key1 != null) {
                upPointJson(name, key1, keyValue1, key2, keyValue2)
            } else {
                upPointJson(name)
            }
            ShowDataTool.showLog("Point-${name}-开始打点--${data}")
            // 执行带重试机制的请求
            val maxNum = if (isAdMinCon) {
                20
            } else {
                3
            }
            executeWithRetry(
                maxRetries = maxNum,
                minDelay = 10_000L,
                maxDelay = 40_000L
            ) { attempt ->
                try {
                    val result = NetTool.executePutRequestSuspend(data)
                    handleSuccess("Point-${name}", result)
                    Result.Success(Unit)
                } catch (e: Exception) {
                    handleError(maxNum, "Point-${name}", e, attempt)
                    Result.Failure(e)
                }
            }
        }
    }

    // 带随机延迟的重试执行器
    private suspend fun <T> executeWithRetry(
        maxRetries: Int,
        minDelay: Long,
        maxDelay: Long,
        block: suspend (attempt: Int) -> Result<T>
    ) {
        repeat(maxRetries) { attempt ->
            when (val result = block(attempt)) {
                is Result.Success -> return
                is Result.Failure -> {
                    val delayTime = Random.nextLong(minDelay, maxDelay)
                    delay(delayTime)
                }
            }
        }
    }

    private suspend fun NetTool.executeAdminRequestSuspend(): String {
        return suspendCancellableCoroutine { continuation ->
            executeAdminRequest(object : NetTool.ResultCallback {
                override fun onComplete(result: String) {
                    continuation.resume(result)
                }

                override fun onError(message: String) {
                    continuation.resumeWithException(Exception(message))
                }
            })
        }
    }

    private suspend fun NetTool.executePutRequestSuspend(data: String): String {
        return suspendCancellableCoroutine { continuation ->
            executePutRequest(data, object : NetTool.ResultCallback {
                override fun onComplete(result: String) {
                    continuation.resume(result)
                }

                override fun onError(message: String) {
                    continuation.resumeWithException(Exception(message))
                }
            })
        }
    }

    // 处理成功响应
    private fun handleSuccess(type: String, result: String) {
        ShowDataTool.showLog("${type}-请求成功: $result")
    }

    // 处理错误日志
    private fun handleError(maxNum: Int, type: String, e: Exception, attempt: Int) {
        ShowDataTool.showLog(
            """
        ${type}-请求失败[重试次数:${attempt + 1}]: ${e.message}
        ${if (attempt >= maxNum - 1) "达到最大重试次数" else ""}
    """.trimIndent()
        )
    }

}