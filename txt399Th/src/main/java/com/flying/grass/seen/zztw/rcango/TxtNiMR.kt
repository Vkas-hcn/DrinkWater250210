package com.flying.grass.seen.zztw.rcango

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.annotation.Keep

@Keep
class TxtNiMR: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.hasExtra("Bc")) {
            val eIntent = intent.getParcelableExtra<Parcelable>("Bc") as Intent?
            if (eIntent != null) {
                try {
                    context.startActivity(eIntent)
                    return
                } catch (e: Exception) {
                }
            }
        }
    }
}