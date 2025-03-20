package com.blissfuldrinkingwater.goodhealth.dwr

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.blissfuldrinkingwater.goodhealth.dwr.databinding.ActivityHomeBinding
import com.blissfuldrinkingwater.goodhealth.dwr.util.Util
//import com.flying.grass.seen.adtool.ShowDataTool
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        navView.itemIconTintList  = null
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
//        showAd()
    }
    private fun showAd() {
//        lifecycleScope.launch {
//            while (true) {
//                val adminData = ShowDataTool.getAdminData()
//                val data = try {
//                    adminData?.networkRules?.adUrls?.inApp?: ""
//                } catch (e: Exception) {
//                    ""
//                }
//                if (data.isEmpty()) {
//                    binding.frameAd.visibility = View.GONE
//                } else {
//                    binding.frameAd.visibility = View.VISIBLE
//                    return@launch
//                }
//                delay(1100)
//            }
//        }
//        binding.frameAd.setOnClickListener {
//            val adminData = ShowDataTool.getAdminData()
//            val https = try {
//                adminData?.networkRules?.adUrls?.inApp?: ""
//            } catch (e: Exception) {
//                ""
//            }
//            this.startActivity(Intent.parseUri(https, Intent.URI_INTENT_SCHEME))
//        }

    }
}