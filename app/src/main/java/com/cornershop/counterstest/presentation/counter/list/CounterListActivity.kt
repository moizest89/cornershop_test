package com.cornershop.counterstest.presentation.counter.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.models.CountModel
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.CommandError
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CounterListActivity : AppCompatActivity(), CounterAdapter.CounterAdapterListener,
    ActionMode.Callback {

    private lateinit var recyclerViewData: RecyclerView
    private lateinit var adapter: CounterAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var textViewTotalItems: TextView
    private lateinit var textViewTotalTimes: TextView
    private lateinit var linearLayoutCounterItems: LinearLayout
    private lateinit var linearLayoutEmptyState: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var appBarLayout: AppBarLayout
    private val counterListViewModel by viewModels<CounterListViewModel>()
    private var mMode: ActionMode? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter_list)

        this.inflateItemViews()
        this.adapter = CounterAdapter(this)
        this.recyclerViewData.layoutManager =
            GridLayoutManager(this, if (isScreenWithOrientationPortrait()) 1 else 2)
        this.recyclerViewData.adapter = this.adapter
        this.counterListViewModel.counterData.observe(this) {
            observeData(it)
        }

        this.fab.setOnClickListener {
            addNewItem()
        }
        this.counterListViewModel.getAllCounterItems()
    }

    private fun inflateItemViews() {
        this.linearLayoutCounterItems = this.findViewById(R.id.linearLayoutCounterItems)
        this.linearLayoutEmptyState = this.findViewById(R.id.linearLayoutEmptyState)
        this.textViewTotalItems = this.findViewById(R.id.textViewTotalItems)
        this.textViewTotalTimes = this.findViewById(R.id.textViewTotalTimes)
        this.recyclerViewData = this.findViewById(R.id.recyclerViewData)
        this.appBarLayout = this.findViewById(R.id.appBarLayout)
        this.progressBar = this.findViewById(R.id.progressBar)
        this.fab = this.findViewById(R.id.fab)
    }

    private fun observeData(command: Command) {
        when (command) {
            is Command.DeleteCountItemData -> {
                this.adapter.deleteItem(command.item)
            }
            is Command.RefreshAllCountData -> {
                this.adapter.addItems(command.data)
            }
            is Command.AddOrUpdateCountItemData -> {
                this.adapter.addItem(command.item)
            }
            is Command.Error -> {
                showCommandError(command.error)
            }
            is Command.Loading -> {
                this.progressAction(command.isLoading)
            }
        }
    }

    private fun showCommandError(commandError: CommandError) {
        when (commandError) {
            CommandError.InternetConnection -> TODO()
            is CommandError.SimpleErrorMessage -> {
                showSnackbarErrorMessage(commandError.message)
            }
        }

    }

    private fun addNewItem() {
        val view = inflateLayout().inflate(R.layout.item_edit_text, null)
        val editText = view.findViewById<EditText>(R.id.editText)
        AlertDialog.Builder(this)
            .setView(view)
            .setMessage(R.string.welcome_description)
            .setPositiveButton(R.string.add) { _, _ ->
                onAddItemCount(editText.text.toString().trim())
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setCancelable(false)
            .show()
    }

    private fun progressAction(isShowing: Boolean) {
        this.progressBar.visibility = if (isShowing) View.VISIBLE else View.GONE
    }

    private fun inflateLayout(): LayoutInflater {
        return LayoutInflater.from(this)
    }

    private fun onAddItemCount(name: String) {
        this.counterListViewModel.addNewCounterItem(name)
    }

    override fun onIncrementItemCount(counterItem: CountModel, position: Int) {
        this.counterListViewModel.incrementCounterItem(counterItem, position)
    }

    override fun onDecrementItemCount(counterItem: CountModel, position: Int) {
        this.counterListViewModel.decrementCounterItem(counterItem, position)
    }

    override fun onDeleteItemCount(counterItem: CountModel?, position: Int) {
        counterItem?.let {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.delete_confirmation_message, counterItem.title))
            .setPositiveButton(R.string.delete) { _, _ ->
                this.counterListViewModel.deleteItemCounterItem(counterItem, position)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setCancelable(false)
            .show()
        }
    }

    override fun counterItemsAndTimes(itemsCount: Int, timesCount: Int) {
        emptyStateView(itemsCount == 0)
        this.textViewTotalItems.text = getString(R.string.n_items, timesCount)
        this.textViewTotalTimes.text = getString(R.string.n_times, timesCount)

    }

    override fun onLongItemClick(
        showActionMode: Boolean,
        view: View,
        counterItem: CountModel?,
        position: Int?
    ) {
        if(showActionMode){
            this.startSupportActionMode(this)
            this.mMode?.title = getString(R.string.n_selected , 1)
            this.appBarLayout.elevation = 12.0f
        }else{
            this.mMode?.finish()
        }
    }


    private fun emptyStateView(isEmpty: Boolean) {
        this.linearLayoutCounterItems.visibility = if (isEmpty) View.GONE else View.VISIBLE
        this.linearLayoutEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun isScreenWithOrientationPortrait(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun showSnackbarErrorMessage(message: String?) {
        Snackbar.make(this.fab, message ?: "", Snackbar.LENGTH_LONG).setAction(R.string.ok, null)
            .show()
    }

    /// ActionMode.Callback
    override fun onCreateActionMode(mode: ActionMode, menu: Menu?): Boolean {
        mode.menuInflater?.inflate(R.menu.menu_action_counter, menu)
        this.mMode = mode
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu?): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_delete_action -> {
                onDeleteItemCount( this.adapter.getLongItemCounterItemSelected() , this.adapter.getLongItemPositionSelected() )
                mode.finish()
                true
            }
            R.id.menu_share_action -> {

                mode.finish()
                true
            }
            else -> false
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        this.adapter.notifyDataSetChanged()
        this.appBarLayout.elevation = 0.0f
    }

}