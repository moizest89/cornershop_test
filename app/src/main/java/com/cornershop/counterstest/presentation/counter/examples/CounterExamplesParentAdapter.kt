package com.cornershop.counterstest.presentation.counter.examples

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R

class CounterExamplesParentAdapter(private val data: List<CounterExamplesActivity.DataItem>, private val onItemClickListener : (String) -> Unit) :
    RecyclerView.Adapter<CounterExamplesParentAdapter.Holder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_counter_example_parent, null)
        )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val itemData = data[position]
        holder.textViewTitle.text = itemData.title
        holder.recyclerViewParentData.layoutManager = LinearLayoutManager(
            holder.recyclerViewParentData.context ,
            RecyclerView.HORIZONTAL ,
            false
        )
        holder.recyclerViewParentData.adapter = CounterExamplesChildAdapter(itemData.items) {
            onItemClickListener.invoke(it)
        }
    }

    override fun getItemCount(): Int = this.data.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val recyclerViewParentData: RecyclerView = itemView.findViewById(R.id.recyclerViewParentData)
    }
}