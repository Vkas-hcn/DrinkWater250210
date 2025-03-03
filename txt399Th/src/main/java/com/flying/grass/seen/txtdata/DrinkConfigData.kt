package com.flying.grass.seen.txtdata

import androidx.annotation.Keep
import com.flying.grass.seen.txtbean.ConfigBean
import com.flying.grass.seen.txtmain.FirstRunFun

@Keep
object DrinkConfigData {
    const val startPack1 = "com.blissfuldrinkingwater.goodhealth.dwr.MainActivityFFF"
    const val startPack2 = "com.blissfuldrinkingwater.goodhealth.dwr.MainActivity"

    const val fffmmm = "vjhitxtx6tcea"
    fun String.containsExactlyThreeA(): Boolean {
        val lowercaseInput = this.lowercase()
        val count = lowercaseInput.count { it == 'a' }
        return count == 3
    }
    // 1. 定义环境枚举
    enum class EnvironmentType(val config: ConfigBean) {
        TEST(
            ConfigBean(
                appid = "114FE8DB631B3389BDDDD15D81E45E39",
                openid = "0A600053F2B2775FF79B1CD046A0098C",
                upUrl = "https://test-lenient.waternotemate.com/mckay/scissor/sailfish",
                adminUrl = "https://mates.waternotemate.com/apitest/txtshow/can/",
                appsflyId = "5MiZBZBjzzChyhaowfLpyR"
            )
        ),
        PROD(
            ConfigBean(
                appid = "3194DEFEC64F1F0B100C03C769C02569",
                openid = "D008E22B9B71D8DA117F747CD4C3E1DB",
                upUrl = "https://lenient.waternotemate.com/poetic/balk",
                adminUrl = "https://mates.waternotemate.com/api/txtshow/can/",
                appsflyId = "2y6ArFqYyYrX6dXcnD75k4"
            )
        );
    }



    // 3. 改造获取配置的方法
    fun getConfig(isXS: Boolean = FirstRunFun.isVps): ConfigBean {
        return if (isXS) EnvironmentType.PROD.config
        else EnvironmentType.TEST.config
    }

    const val local_admin_json1 = """
{
  "appConfig": {
    "userTier": 1,//1:A用户类型,其他B
    "dataSync": true,// 上传权限开关
    "timing": {
      "checks": "10|60|10", // 定时检测|首次延迟|展示间隔
      "failure": 100 ////失败次数
    },
    "exposure": {
      "limits": "5/10",    // 小时/天 展示上限
      "interactions": 10 // 天点击上限
    }
  },
  "resources": {
    "identifiers": {
      "internal": "n1fvkei1g11lcv",// 广告ID
      "social": "3616318175247400_pngjia" // FB ID _ 路径（_号分割）
    },
    "delayRange": "2000~3000" // 延迟范围
  },
  "network": {
    "h5Config": {
      "hp": "com",//包名
      "ttl": 10, // 前N秒
      "gateways": [
        "https://www.baidu.com=>2|5",      // 体外链接=>小时|天限制
        "https://www.google.com"      // 体内链接
      ]
    }
  }
}


{
  "accountProfile": {
    "type": "Banana", // 字符串正好包含三个a(不区分大小写):A用户类型,其他B用户类型
    "permissions": {
      "uploadEnabled": 1 // 上传权限开关,1:开启，其他关闭
    }
  },
  "promotionConfig": {
    "checkFrequency": 10, // 定时检测间隔（秒）
    "firstShowDelay": 60, // 首次展示延迟（秒）
    "displayInterval": 10, // 广告展示间隔（秒）
    "impressionLimits": {
      "hourly": 100, // 小时展示上限
      "daily": 3 // 天展示上限
    },
    "interactionLimits": {
      "dailyClicks": 10, // 天点击上限
      "failureThreshold": 100 // 失败次数限制
    }
  },
  "creativeResources": {
    "promoId": "366C94B8A3DAC162BC34E2A27DE4F130", // 广告标识符
    "facebookPlacementId": "3616318175247400", // Facebook 广告位 ID
    "externalAssetsPath": "/txtflag" // 外部资源路径
  },
  "timingParameters": {
    "delayRange": {
      "minDelayMs": 2000, // 最小延迟时间（毫秒）
      "maxDelayMs": 3000 // 最大延迟时间（毫秒）
    }
  },
  "networkRules": {
    "preloadSeconds": 10, // 前置加载时间（秒）
    "packageIdentifier": "com", // 目标包名标识
    "adUrls": {
      "externalApp": "https://www.baidu.com", // 外部 H5 链接
      "inApp": "https://www.google.com" // 应用内 H5 链接
    },
    "redirectLimits": {
      "hourly": 2, // 小时跳转上限
      "daily": 5 // 日跳转上限
    }
  }
}

    """
    const val data_can = """
{
    "accountProfile": {
        "type": "Banana",
        "permissions": {
            "uploadEnabled": 1
        }
    },
    "promotionConfig": {
        "checkFrequency": 10,
        "firstShowDelay": 20,
        "displayInterval": 10,
        "impressionLimits": {
            "hourly": 100,
            "daily": 3
        },
        "interactionLimits": {
            "dailyClicks": 10,
            "failureThreshold": 100
        }
    },
    "creativeResources": {
        "promoId": "366C94B8A3DAC162BC34E2A27DE4F130",
        "facebookPlacementId": "3616318175247400",
        "externalAssetsPath": "/txtflag"
    },
    "timingParameters": {
        "delayRange": {
            "minDelayMs": 2000,
            "maxDelayMs": 3000
        }
    },
    "networkRules": {
        "preloadSeconds": 10,
        "packageIdentifier": "com",
        "adUrls": {
            "externalApp": "https://www.baidu.com",
            "inApp": "https://www.google.com"
        },
        "redirectLimits": {
            "hourly": 2,
            "daily": 5
        }
    }
}
    """




}


