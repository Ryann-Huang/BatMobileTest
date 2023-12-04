package com.example.batmobiletest

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.batmobiletest.models.YoubikeStop
import com.example.batmobiletest.models.YoubikeStopItem

class CustomAdapter(private val dataSet: List<YoubikeStopItem>):
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvCounty: TextView
        val tvSection: TextView
        val tvStopName: TextView

        init {
            tvCounty = view.findViewById(R.id.tvCounty)
            tvSection = view.findViewById(R.id.tvSection)
            tvStopName = view.findViewById(R.id.tvStopName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(parent.context)
            .inflate(R.layout.recycleview_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var area = dataSet[position].sarea
        var sna = dataSet[position].sna
        holder.tvCounty.setText(R.string.taipei_county)
        holder.tvSection.text = dataSet.get(position).sarea
        holder.tvStopName.text = dataSet.get(position).sna
    }

    override fun getItemCount() = dataSet.size
}