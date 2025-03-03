package com.blissfuldrinkingwater.goodhealth.dwr.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blissfuldrinkingwater.goodhealth.dwr.bean.InBean
import com.blissfuldrinkingwater.goodhealth.dwr.databinding.ItemDetailBinding

class DetailAdapter public constructor(val mContext: Context, val itemList:MutableList<InBean>): RecyclerView.Adapter<DetailAdapter.VH>(){
    inner class VH public constructor(val bin:ItemDetailBinding) : RecyclerView.ViewHolder(bin.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemDetailBinding.inflate(LayoutInflater.from(mContext),parent,false))
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(vh: VH, position: Int) {
        val data = itemList.get(position)
        vh.bin.time.text = data.timeStr
        vh.bin.vale.text ="${data.vale}ml"
        vh.bin.pg.progress = (data.vale*100/data.target)
    }
}