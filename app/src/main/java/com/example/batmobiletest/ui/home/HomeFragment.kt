package com.example.batmobiletest.ui.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.batmobiletest.CustomAdapter
import com.example.batmobiletest.R
import com.example.batmobiletest.databinding.FragmentHomeBinding
import com.example.batmobiletest.inputhistory.InputHistoryManager
import com.example.batmobiletest.models.YoubikeStop
import com.example.batmobiletest.models.YoubikeStopItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.lang.StringBuilder

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var customAdapter: CustomAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var etInput: EditText
    private lateinit var btnSearch: Button

    private lateinit var inputHistoryMgr: InputHistoryManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        subscribe()
        homeViewModel.getData()

        inputHistoryMgr = InputHistoryManager(context)
        lifecycleScope.launch(Dispatchers.IO) {
            inputHistoryMgr.getInputHistoryPrefData()
        }

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.tvStopInfo
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        etInput = binding.etInput
        etInput.setOnClickListener { _ ->
            showListPopupWindow()
        }

        btnSearch = binding.btnSearch
        btnSearch.setOnClickListener { _ ->
            val str = etInput.text.toString()
            if (!str.isNullOrBlank()) {
                lifecycleScope.launch {
                    launch {
                        var listStops = getDataFromSearchText(etInput.text.toString())
                        if (listStops.size != 0) {
                            customAdapter = CustomAdapter(listStops)
                            recyclerView = binding.rvData
                            recyclerView.adapter = customAdapter
                        }
                    }
                    launch {
                        inputHistoryMgr.addInputHistory(etInput.text.toString())
                    }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun subscribe() {
        homeViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                Log.d("BatMobile", "Retrieving data...")
            }
        }

        homeViewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError) {
                Log.e("BatMobile", "Error: Fail to retrieve data")
            }
        }

        homeViewModel.youbikeStopData.observe(viewLifecycleOwner) { youbikeStopData ->
            //Display weather data to the UI
            //adapter? listview?
            customAdapter = CustomAdapter(youbikeStopData)
            recyclerView = binding.rvData
            recyclerView.adapter = customAdapter
        }
    }

    private fun showListPopupWindow() {
        var list = inputHistoryMgr.getInputHistoryList()
        if (list.size == 0)
            return
        var listPopupWindow = context?.let { ListPopupWindow(it) }
        listPopupWindow?.setAdapter(context?.let {
            ArrayAdapter<String>(
                it, android.R.layout.simple_list_item_1, list
            )
        })

        listPopupWindow?.anchorView = etInput
        listPopupWindow?.isModal = true

        listPopupWindow?.setOnItemClickListener { parent, view, position, id ->
            etInput.setText(list[position])
            listPopupWindow.dismiss()
        }

        listPopupWindow?.show()
    }

    fun getDataFromSearchText(strText: String): ArrayList<YoubikeStopItem> {
        var listStop = ArrayList<YoubikeStopItem>()
        for (stopInfo in homeViewModel.youbikeStopData.value!!) {
            if (stopInfo.sarea == strText) {
                listStop.add(stopInfo)
            }
        }

        return listStop
    }
}
