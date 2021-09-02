package com.cornershop.counterstest.presentation.counter.examples

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R

class CounterExamplesChildAdapter(private val data: List<String>, private val onItemClickListener : (String) -> Unit) :
    RecyclerView.Adapter<CounterExamplesChildAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_counter_example_child, null)
    )


    override fun getItemCount(): Int = this.data.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.textViewTitle.text = data[position]
        holder.textViewTitle.setOnClickListener {
            onItemClickListener.invoke(data[position])
        }
    }
}