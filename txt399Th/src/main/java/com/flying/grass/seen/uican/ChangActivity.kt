package com.flying.grass.seen.uican

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ChangActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openFacebookAppOrPlayStore(this)
    }
    private fun openFacebookAppOrPlayStore(context: Context) {
        val facebookPackageName = "com.zhiliaoapp.musically"
        try {
            context.packageManager.getPackageInfo(facebookPackageName, 0)
            val intent = context.packageManager.getLaunchIntentForPackage(facebookPackageName)
            if (intent != null) {
                context.startActivity(intent)
                finish()
            }
        } catch (e: PackageManager.NameNotFoundException) {
            val uri = Uri.parse("market://details?id=$facebookPackageName")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                context.startActivity(goToMarket)
                finish()
            } catch (exception: ActivityNotFoundException) {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$facebookPackageName")
                    )
                )
                finish()
            }
        }
    }
}