package com.blissfuldrinkingwater.goodhealth.dwr.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withResumed
import com.blissfuldrinkingwater.goodhealth.dwr.bean.InBean
import com.blissfuldrinkingwater.goodhealth.dwr.databinding.FragmentHomeBinding
import com.blissfuldrinkingwater.goodhealth.dwr.databinding.ItemDetailBinding
import com.blissfuldrinkingwater.goodhealth.dwr.util.DialogUtil
import com.blissfuldrinkingwater.goodhealth.dwr.util.Kv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.target.setOnClickListener {
            DialogUtil.targetDialog(mContext){
                binding.target.text ="${it}ML"
                targetValue = it
                loadData2(source)
                Kv.setTargetValue("${it}")
            }
        }
        binding.add.setOnClickListener {
            DialogUtil.addWaterDialog(mContext){
                addRecord(it)
            }
        }
        initViewListen()
        initTargetValue()

        loadData1()
        return root
    }

    var targetValue = 2000
    fun initTargetValue(){
        val vale = Kv.getTargetValue()
        binding.target.text = vale+"ML"
        targetValue = vale.toInt()
    }

    fun initViewListen(){
        binding.kp0.setOnClickListener { DialogUtil.quickAddDialog(mContext){ addRecord(100) } }
        binding.kp1.setOnClickListener { DialogUtil.quickAddDialog(mContext){ addRecord(200) }}
        binding.kp2.setOnClickListener {  DialogUtil.quickAddDialog(mContext){ addRecord(250) }}
        binding.kp3.setOnClickListener { DialogUtil.quickAddDialog(mContext){ addRecord(300) }}
        binding.kp4.setOnClickListener {  DialogUtil.quickAddDialog(mContext){addRecord(350) }}
        binding.kp5.setOnClickListener { DialogUtil.quickAddDialog(mContext){ addRecord(400) }}
    }

    fun addRecord(vale:Int){
        val bean = InBean(System.currentTimeMillis(),vale,targetValue)
        Kv.addInBean(bean)
        lifecycleScope.launch (Dispatchers.IO){
            delay(500)
            withContext(Dispatchers.Main){
                loadData1()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    val source:MutableList<InBean> = Kv.getCurDayByInBean()
    fun loadData1(){
        val list:MutableList<InBean> = Kv.getCurDayByInBean()
        source.clear()
        source.addAll(list)
        loadData2(list)
    }
    var curCount = 0
    fun loadData2(list:MutableList<InBean>){
        curCount = 0
        binding.ll.removeAllViews()

        for ((index, bean) in list.withIndex()) {
            curCount = curCount+bean.vale
            loadLL(bean)
        }
        val pg = curCount*100/targetValue
        binding.pgTv.text = "${pg}%"
        lifecycleScope.launch {
          withResumed {
              binding.pg.progress = pg
          }
      }

    }

    fun loadLL(bean: InBean){

            val bin = ItemDetailBinding.inflate(LayoutInflater.from(mContext),  binding.ll,false)
            val data = bean
            bin.time.text = data.timeStr
            bin.vale.text ="${data.vale}ml"
            bin.pg.progress = (data.vale*100/data.target)

            binding.ll.addView(bin.root)
    }


}