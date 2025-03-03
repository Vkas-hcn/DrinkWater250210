package com.flying.grass.seen.txtbean

import androidx.annotation.Keep

@Keep
data class ConfigBean(
    val appid: String,
    val openid: String,
    val upUrl: String,
    val adminUrl: String,
    val appsflyId: String
)
