package com.blissfuldrinkingwater.goodhealth.dwr.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.blissfuldrinkingwater.goodhealth.dwr.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Util {
    fun toast( msg:String){
//        Toast.makeText(App.app,msg,Toast.LENGTH_SHORT).show()
//        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
      makeText(App.app,msg,  Toast.LENGTH_SHORT).show();
    }
    fun makeText(context: Context, text: String?, duration: Int): Toast {
        val result = Toast.makeText(context, text, duration)
        val toastView: View =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.transient_notification,
                null
            )
        val textView = toastView.findViewById<TextView>(R.id.text)
        textView.text = text
        result.setView(toastView)
        return result
    }
    inline fun i(call:() -> String){
//        Log.i("HHHH",call.invoke())
    }
    fun dateFormat(ms: Long): String {
        val str = SimpleDateFormat("MMM d yyyy", Locale.getDefault()).format(Date(ms))
        return str
    }
    fun dateFormat2(ms: Long): List<String> {
        val str = SimpleDateFormat("MMM d yyyy#HH:mm", Locale.getDefault()).format(Date(ms))
        return str.split("#")
    }

}