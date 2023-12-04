package com.example.batmobiletest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.batmobiletest.databinding.FragmentOptionsBinding

class OptionsFragment: Fragment() {

    private var _binding: FragmentOptionsBinding? = null

    private val binding get() = _binding!!

    private lateinit var btnStopInfo:Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOptionsBinding.inflate(inflater, container, false)
        var root: View = binding.root

        btnStopInfo = binding.btnStopInfo
        btnStopInfo.setOnClickListener { _ ->
            val navController = root.findNavController()
            navController.navigateUp()
        }
        return root
    }
}