package com.blissfuldrinkingwater.goodhealth.dwr.bean

class GpBean public constructor(val dateStr:String, val list: MutableList<InBean>) {

    var target = 0 // dang tan mu biao
    var count = 0
    var cups = 0
    var rate = 0
    init {
        list.sortBy { it.time }
        target = list.last().target
        count = list.sumOf { it.vale }
        cups = list.size
        rate = count*100/target
    }
}