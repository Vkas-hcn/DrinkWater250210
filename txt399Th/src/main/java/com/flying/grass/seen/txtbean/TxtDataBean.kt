package com.flying.grass.seen.txtbean

import androidx.annotation.Keep

@Keep
data class TxtDataBean(
    val accountProfile: AccountProfile,
    val creativeResources: CreativeResources,
    val networkRules: NetworkRules,
    val promotionConfig: PromotionConfig,
    val timingParameters: TimingParameters
)

@Keep
data class AccountProfile(
    val permissions: Permissions,
    val type: String
)

@Keep
data class CreativeResources(
    val externalAssetsPath: String,
    val facebookPlacementId: String,
    val promoId: String
)

@Keep
data class NetworkRules(
    val adUrls: AdUrls,
    val packageIdentifier: String,
    val preloadSeconds: Int,
    val redirectLimits: RedirectLimits
)

@Keep
data class PromotionConfig(
    val checkFrequency: Int,
    val displayInterval: Int,
    val firstShowDelay: Int,
    val impressionLimits: ImpressionLimits,
    val interactionLimits: InteractionLimits
)

@Keep
data class TimingParameters(
    val delayRange: DelayRange
)

@Keep
data class Permissions(
    val uploadEnabled: Int
)

@Keep
data class AdUrls(
    val externalApp: String,
    val inApp: String
)

@Keep
data class RedirectLimits(
    val daily: Int,
    val hourly: Int
)

@Keep
data class ImpressionLimits(
    val daily: Int,
    val hourly: Int
)

@Keep
data class InteractionLimits(
    val dailyClicks: Int,
    val failureThreshold: Int
)

@Keep
data class DelayRange(
    val maxDelayMs: Int,
    val minDelayMs: Int
)


