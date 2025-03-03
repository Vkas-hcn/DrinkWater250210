package com.blissfuldrinkingwater.goodhealth.dwr.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.blissfuldrinkingwater.goodhealth.dwr.databinding.DialogQuickAddBinding
import com.blissfuldrinkingwater.goodhealth.dwr.databinding.DialogTargetBinding

object DialogUtil {

    fun targetDialog(mContext: Context,call:(Int)->Unit){

        val dialog = Dialog(mContext)
        val bin = DialogTargetBinding.inflate(LayoutInflater.from(mContext))
        bin.cancel.setOnClickListener { dialog.dismiss() }
        bin.confirm.setOnClickListener {
            val ss = bin.input.text.toString().trim()
            if (ss.isEmpty()){
                Util.toast("Please enter today's drinking water target")
            }else{
                dialog.dismiss()
                call.invoke(ss.toInt())
            }
        }
        dialog.setContentView(bin.root)
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialog.show()
    }

    fun addWaterDialog(mContext: Context,call:(Int)->Unit){

        val dialog = Dialog(mContext)
        val bin = DialogTargetBinding.inflate(LayoutInflater.from(mContext))
        bin.cancel.setOnClickListener { dialog.dismiss() }
        bin.confirm.setOnClickListener {
            val ss = bin.input.text.toString().trim()
            if (ss.isEmpty()){
                Util.toast("Please enter today's drinking water target")
            }else{
                dialog.dismiss()
                call.invoke(ss.toInt())
            }
        }
        dialog.setContentView(bin.root)
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialog.show()
    }

    fun quickAddDialog(mContext: Context,call:(Int)->Unit){
        val dialog = Dialog(mContext)
        val bin = DialogQuickAddBinding.inflate(LayoutInflater.from(mContext))
        bin.cancel.setOnClickListener { dialog.dismiss() }
        bin.confirm.setOnClickListener {
                dialog.dismiss()
                call.invoke(100)
        }
        dialog.setContentView(bin.root)
        dialog.window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialog.show()
    }
}