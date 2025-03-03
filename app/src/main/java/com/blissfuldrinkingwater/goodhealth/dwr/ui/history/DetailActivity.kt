package com.blissfuldrinkingwater.goodhealth.dwr.ui.history

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.blissfuldrinkingwater.goodhealth.dwr.bean.GpBean
import com.blissfuldrinkingwater.goodhealth.dwr.bean.InBean
import com.blissfuldrinkingwater.goodhealth.dwr.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    companion object {
        var tmpData: GpBean? = null
        fun start(mContext: Context, data: GpBean){
            tmpData = data
            mContext.startActivity(Intent(mContext,DetailActivity::class.java))
        }
    }

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener { finish() }

        loadData()
    }

    fun  loadData(){
        val mData =  tmpData
        if (mData!=null){
            itemList.addAll(mData.list)
            initRv()

            binding.target.text = "Target: ${mData.target}ml"
            binding.rate.text = "${mData.rate}%"
            binding.pg.progress = mData.rate
            binding.cups.text = "${mData.cups} Cups"
            binding.countTv.text = "${mData.count} ML"
        }
    }

    val itemList = mutableListOf<InBean>()
    fun initRv(){
        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = DetailAdapter(this,itemList)
    }


}