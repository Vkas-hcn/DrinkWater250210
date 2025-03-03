package com.blissfuldrinkingwater.goodhealth.dwr.ui.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blissfuldrinkingwater.goodhealth.dwr.bean.GpBean
import com.blissfuldrinkingwater.goodhealth.dwr.databinding.ItemHistoryBinding

class HistoryAdapter public constructor(val mContext: Context,val itemList:MutableList<GpBean>): RecyclerView.Adapter<HistoryAdapter.VH>(){
    inner class VH public constructor(val bin:ItemHistoryBinding) : RecyclerView.ViewHolder(bin.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemHistoryBinding.inflate(LayoutInflater.from(mContext),parent,false))
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(vh: VH, position: Int) {
        val data = itemList.get(position)
        vh.bin.dateTv.text = data.dateStr
        vh.bin.vale.text ="${data.count}ml"
        vh.bin.cups.text = "${data.cups}"
        vh.bin.pg.progress = data.rate

        vh.itemView.setOnClickListener {
            DetailActivity.start(mContext,data)
        }

    }
}