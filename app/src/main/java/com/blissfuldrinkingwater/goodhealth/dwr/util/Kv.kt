package com.blissfuldrinkingwater.goodhealth.dwr.util

import com.blissfuldrinkingwater.goodhealth.dwr.bean.GpBean
import com.blissfuldrinkingwater.goodhealth.dwr.bean.InBean
import com.tencent.mmkv.MMKV

object Kv {

    private lateinit var mmkv: MMKV

    fun init(app:App){
        MMKV.initialize(app)
        mmkv = MMKV.defaultMMKV()
    }

    private fun putString(key: String, value: String) {
        mmkv.encode(key, value)
    }
    private fun getString(key: String): String {
        return mmkv.decodeString(key) ?: ""
    }

    private fun putSet(key: String, value: Set<String>) {
        mmkv.encode(key, value)
    }
    private  fun getSet(key: String): MutableSet<String> {
        return mmkv.decodeStringSet(key) ?: mutableSetOf()
    }

    private val KEY_TARGET_VALUE = "key_target_value"
    fun setTargetValue(value:String){
        putString(KEY_TARGET_VALUE,value)
    }
    fun getTargetValue(): String {
        return getString(KEY_TARGET_VALUE)
    }
    fun initTargetValue(){
        val value =  getTargetValue()
        if (value.isEmpty()){
            setTargetValue("2000")
        }
    }

    val KEY_INBEAN_LIST = "key_inbean_list"
    fun addInBean(bean: InBean){
        val set = getSet(KEY_INBEAN_LIST)
        set.add(bean.text())
        putSet(KEY_INBEAN_LIST,set)
    }

//    fun rmInBean(bean: InBean){
//        val set = getSet(KEY_INBEAN_LIST)
//        val ite = set.iterator()
//        while (ite.hasNext()){
//            val str = ite.next()
//            if (str.startsWith("${bean.time}")){
//                ite.remove()
//                break
//            }
//        }
//        putSet(KEY_INBEAN_LIST,set)
//    }


    fun getDateByInBean(): MutableMap<String, MutableList<InBean>> {
        val set = getSet(KEY_INBEAN_LIST)

        val map = mutableMapOf<String,MutableList<InBean>>()

        val ite = set.iterator()
        while (ite.hasNext()){
            val str = ite.next()
            val array = str.split("#")
            val bean = InBean(array.get(0).toLong(),array.get(1).toInt(),array.get(2).toInt())

            val tmpList = map.get(bean.dateStr)
            if (tmpList.isNullOrEmpty()){
                map.put(bean.dateStr, mutableListOf(bean))
            }else{
                tmpList.add(bean)
            }
        }

        return map
    }


    fun getCurDayByInBean(): MutableList<InBean> {
        val curDayDate = Util.dateFormat(System.currentTimeMillis())

        val list = mutableListOf<InBean>()

        val set = getSet(KEY_INBEAN_LIST)
        val ite = set.iterator()
        while (ite.hasNext()){
            val str = ite.next()
            val array = str.split("#")
            val bean = InBean(array.get(0).toLong(),array.get(1).toInt(),array.get(2).toInt())

            if (curDayDate.equals(bean.dateStr)){
                list.add(bean)
            }
        }

        list.sortBy { it.time }

        return list
    }
}