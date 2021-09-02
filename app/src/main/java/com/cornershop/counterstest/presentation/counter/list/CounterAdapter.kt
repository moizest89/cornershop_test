package com.cornershop.counterstest.presentation.counter.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.models.CountModel
import com.google.android.material.button.MaterialButton

class CounterAdapter(private val onCounterListener: CounterAdapterListener) :
    RecyclerView.Adapter<CounterAdapter.Holder>() {

    private val counterList = mutableListOf<CountModel>()
    private val counterItemSelectedList = mutableListOf<CountModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_counter_list, null))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val itemCounter = counterList[position]
        holder.cardViewMainContainer.setCardBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                R.color.white
            )
        )
        holder.textViewCounterName.text = itemCounter.title
        holder.textViewCounter.text = itemCounter.count.toString()

        holder.imageViewCheck.visibility = View.GONE
        holder.materialButtonIncrement.visibility = View.VISIBLE
        holder.materialButtonDecrement.visibility = View.VISIBLE
        holder.textViewCounter.visibility = View.VISIBLE

        //Increment
        holder.materialButtonIncrement.setOnClickListener {
            this.onCounterListener.onIncrementItemCount(
                itemCounter, position
            )
        }
        //Decrement
        val moreThanZero = itemCounter.count > 0
        holder.materialButtonDecrement.isEnabled = moreThanZero
        holder.materialButtonDecrement.alpha = if (moreThanZero) 1.0f else 0.2f
        holder.materialButtonDecrement.setOnClickListener {
            this.onCounterListener.onDecrementItemCount(
                itemCounter, position
            )
        }

        holder.itemView.setOnLongClickListener { v ->
            onEvaluateSelectedItem(holder, itemCounter)
            true
        }
        holder.itemView.setOnClickListener { v ->
            if (onLongItemSelectedActive) {
                onEvaluateSelectedItem(holder, itemCounter)
            }
        }

    }

    private fun onEvaluateSelectedItem(holder: Holder, itemCounter: CountModel) {
        if (counterItemSelectedList.contains(itemCounter)) {
            onDrawUnselectedItem(holder)
            counterItemSelectedList.remove(itemCounter)
        } else {
            onDrawSelectedItem(holder)
            counterItemSelectedList.add(itemCounter)
        }
        onLongItemSelectedActive = counterItemSelectedList.isNotEmpty()
        onCounterListener.onLongItemClick(onLongItemSelectedActive, counterItemSelectedList)
    }

    private fun onDrawSelectedItem(holder: Holder) {
        holder.cardViewMainContainer.setCardBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                R.color.orange_20
            )
        )
        holder.imageViewCheck.visibility = View.VISIBLE
        holder.materialButtonIncrement.visibility = View.GONE
        holder.materialButtonDecrement.visibility = View.GONE
        holder.textViewCounter.visibility = View.GONE
    }

    private fun onDrawUnselectedItem(holder: Holder) {
        holder.cardViewMainContainer.setCardBackgroundColor(
            ContextCompat.getColor(
                holder.itemView.context,
                R.color.white
            )
        )
        holder.imageViewCheck.visibility = View.GONE
        holder.materialButtonIncrement.visibility = View.VISIBLE
        holder.materialButtonDecrement.visibility = View.VISIBLE
        holder.textViewCounter.visibility = View.VISIBLE
    }

    fun addItem(countItem: CountModel?) {
        if (countItem != null) {
            if (this.counterList.isEmpty()) {
                this.addItems(mutableListOf(countItem))
            } else {
                val indexInList = this.counterList.indexOfFirst { it.id == countItem.id }
                if (indexInList != -1) {
                    this.counterList.removeAt(indexInList)
                    this.counterList.add(indexInList, countItem)
                    this.notifyItemChanged(indexInList)
                } else if (indexInList == -1) {
                    this.counterList.add(countItem)
                    this.notifyItemInserted(this.counterList.size - 1)
                }
            }
        }
        this.onTotalSumCounter(this.counterList)
    }

    fun addItems(countItems: MutableList<CountModel>) {
        this.counterList.clear()
        this.counterList.addAll(countItems)
        this.notifyDataSetChanged()
        this.onTotalSumCounter(this.counterList)
    }

    fun deleteItem(countItem: CountModel?) {
        if (countItem != null) {
            val indexInList = this.counterList.indexOfFirst { it.id == countItem.id }
            if (indexInList != -1) {
                this.counterList.removeAt(indexInList)
                this.notifyItemRemoved(indexInList)
            }
        }
        this.onTotalSumCounter(this.counterList)
    }


    fun getItemsCounterSelected(): MutableList<CountModel> {
        return counterItemSelectedList
    }

    fun resetItemsCounterSelected() {
        this.counterItemSelectedList.clear()
        onLongItemSelectedActive = false
        notifyDataSetChanged()
    }

    private fun onTotalSumCounter(counterList: MutableList<CountModel>) {
        val timesCount = counterList.sumOf { it.count }
        onCounterListener.counterItemsAndTimes(counterList.size, timesCount)
    }

    override fun getItemCount(): Int = this.counterList.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val materialButtonIncrement: MaterialButton =
            itemView.findViewById(R.id.materialButtonIncrement)
        val materialButtonDecrement: MaterialButton =
            itemView.findViewById(R.id.materialButtonDecrement)
        val textViewCounterName: TextView = itemView.findViewById(R.id.textViewCounterName)
        val textViewCounter: TextView = itemView.findViewById(R.id.textViewCounter)
        val cardViewMainContainer: CardView = itemView.findViewById(R.id.cardViewMainContainer)
        val imageViewCheck: ImageView = itemView.findViewById(R.id.imageViewCheck)
    }

    interface CounterAdapterListener {
        fun onIncrementItemCount(counterItem: CountModel, position: Int)
        fun onDecrementItemCount(counterItem: CountModel, position: Int)
        fun onDeleteItemCount(counterItem: CountModel, position: Int)
        fun onDeleteItemsCount(counterItems: MutableList<CountModel>)
        fun counterItemsAndTimes(itemsCount: Int, timesCount: Int)
        fun onLongItemClick(
            showActionMode: Boolean,
            counterItemsSelected: MutableList<CountModel>
        )
    }

    companion object {
        private var onLongItemSelectedActive = false
    }
}