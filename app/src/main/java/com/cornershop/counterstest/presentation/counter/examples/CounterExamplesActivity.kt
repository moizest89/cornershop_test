package com.cornershop.counterstest.presentation.counter.examples

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.presentation.base.BaseActivity
import com.cornershop.counterstest.presentation.utils.Utils

class CounterExamplesActivity : BaseActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var adapter: CounterExamplesParentAdapter
    private lateinit var recyclerViewData: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter_examples)
        this.inflateViews()
        this.createToolbar()
        this.setData()
    }

    private fun setData() {
        this.adapter = CounterExamplesParentAdapter(getData()) {
            onItemSelected(it)
        }
        this.recyclerViewData.layoutManager = LinearLayoutManager(this)
        this.recyclerViewData.adapter = this.adapter
    }

    private fun onItemSelected(itemSelected: String) {
        val bundle = Bundle()
        bundle.putString(Utils.DATA, itemSelected)
        val intent = Intent()
        intent.putExtras(bundle)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun inflateViews() {
        this.toolbar = this.findViewById(R.id.toolbar)
        this.recyclerViewData = this.findViewById(R.id.recyclerViewData)
    }

    private fun createToolbar() {
        toolbar.title = getString(R.string.examples)
        toolbar.navigationIcon?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                ContextCompat.getColor(this, R.color.orange), BlendModeCompat.SRC_ATOP
            )
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }


    private fun getData(): List<DataItem> {
        return listOf(
            DataItem(
                title = getString(R.string.drinks_title),
                items = resources.getStringArray(R.array.drinks_array).toMutableList()
            ),
            DataItem(
                title = getString(R.string.food_title),
                items = resources.getStringArray(R.array.food_array).toMutableList()
            ),
            DataItem(
                title = getString(R.string.misc_title),
                items = resources.getStringArray(R.array.misc_array).toMutableList()
            )
        )
    }

    data class DataItem(
        var title: String = "",
        var items: List<String> = listOf()
    )

}