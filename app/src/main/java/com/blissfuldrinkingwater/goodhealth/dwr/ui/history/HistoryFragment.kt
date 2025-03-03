package com.blissfuldrinkingwater.goodhealth.dwr.ui.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blissfuldrinkingwater.goodhealth.dwr.bean.GpBean
import com.blissfuldrinkingwater.goodhealth.dwr.bean.InBean
import com.blissfuldrinkingwater.goodhealth.dwr.databinding.FragmentHistoryBinding
import com.blissfuldrinkingwater.goodhealth.dwr.util.Kv

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root


        initRv()
        loadData()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    var allDayCount = 0
//    var allDayRate = 0
    fun loadData(){
      val map:MutableMap<String, MutableList<InBean>> = Kv.getDateByInBean()

        val list = mutableListOf<GpBean>()
        val mapIte = map.iterator()
        while (mapIte.hasNext()){
            val enter = mapIte.next()
            val bean = GpBean(enter.key,enter.value)
            allDayCount = allDayCount+bean.count
//            allDayRate = allDayRate+bean.rate
            list.add(bean)
        }
        list.sortByDescending { it.dateStr }

        updateRv(list)
        updateUI()
    }

    val itemList = mutableListOf<GpBean>()
    fun updateRv(list:MutableList<GpBean>){
        itemList.clear()
        itemList.addAll(list)
    }

    fun updateUI(){
        if (itemList.isEmpty()){

        }else{
            binding.days.text = "${itemList.size}"
            binding.avg.text = "${allDayCount/itemList.size} ML"
        }
    }

    fun initRv(){
        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager = LinearLayoutManager(mContext)
        binding.rv.adapter = HistoryAdapter(mContext,itemList)
    }
}