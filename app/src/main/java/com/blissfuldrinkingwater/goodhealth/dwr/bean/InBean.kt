package com.blissfuldrinkingwater.goodhealth.dwr.bean

import com.blissfuldrinkingwater.goodhealth.dwr.util.Util

class InBean public constructor(val time:Long,val vale:Int,val target:Int) {
    fun text(): String {
        return "$time#$vale#$target"
    }

    var dateStr = ""
    var timeStr = ""

    init {
        val list = Util.dateFormat2(time)
        dateStr =  list.get(0)
        timeStr =  list.get(1)
    }


}