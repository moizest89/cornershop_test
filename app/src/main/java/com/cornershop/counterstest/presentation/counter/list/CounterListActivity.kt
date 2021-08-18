package com.cornershop.counterstest.presentation.counter.list

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ActionMode
import androidx.cardview.widget.CardView
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.models.CountModel
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.CommandError
import com.cornershop.counterstest.presentation.base.BaseActivity
import com.cornershop.counterstest.presentation.counter.add.CounterAddActivity
import com.cornershop.counterstest.presentation.utils.NetworkUtils
import com.cornershop.counterstest.presentation.utils.Utils
import com.cornershop.counterstest.presentation.utils.hideKeyboard
import com.cornershop.counterstest.presentation.utils.onTextChange
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CounterListActivity : BaseActivity(), CounterAdapter.CounterAdapterListener,
    ActionMode.Callback, ActivityResultCallback<ActivityResult> {

    @Inject
    lateinit var networkUtils: NetworkUtils

    private lateinit var linearLayoutCounterItems: LinearLayout
    private lateinit var linearLayoutInternetState: LinearLayout
    private lateinit var editTextSearchInformation: EditText
    private lateinit var linearLayoutEmptyState: LinearLayout
    private lateinit var cardViewSearchCounter: CardView
    private lateinit var imageViewRemoveFocus: ImageView
    private lateinit var textViewSearchResult: TextView
    private lateinit var materialButtonRetry: MaterialButton
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var textViewTotalItems: TextView
    private lateinit var textViewTotalTimes: TextView
    private lateinit var recyclerViewData: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var adapter: CounterAdapter
    private lateinit var fab: ExtendedFloatingActionButton

    private val counterListViewModel by viewModels<CounterListViewModel>()
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(), this)
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
        this.materialButtonRetry.setOnClickListener {
            this.initRequest()
        }
        this.swipeRefreshLayout.setColorSchemeColors(getColor(R.color.orange))
        this.swipeRefreshLayout.setOnRefreshListener {
            this.initRequest()
        }
        this.initRequest()
        this.searchCounterItemInList()
    }

    private fun inflateItemViews() {
        this.linearLayoutCounterItems = this.findViewById(R.id.linearLayoutCounterItems)
        this.linearLayoutInternetState = this.findViewById(R.id.linearLayoutInternetState)
        this.editTextSearchInformation = this.findViewById(R.id.editTextSearchInformation)
        this.linearLayoutEmptyState = this.findViewById(R.id.linearLayoutEmptyState)
        this.cardViewSearchCounter = this.findViewById(R.id.cardViewSearchCounter)
        this.textViewSearchResult = this.findViewById(R.id.textViewSearchResult)
        this.imageViewRemoveFocus = this.findViewById(R.id.imageViewRemoveFocus)
        this.materialButtonRetry = this.findViewById(R.id.materialButtonRetry)
        this.swipeRefreshLayout = this.findViewById(R.id.swipeRefreshLayout)
        this.textViewTotalItems = this.findViewById(R.id.textViewTotalItems)
        this.textViewTotalTimes = this.findViewById(R.id.textViewTotalTimes)
        this.recyclerViewData = this.findViewById(R.id.recyclerViewData)
        this.appBarLayout = this.findViewById(R.id.appBarLayout)
        this.progressBar = this.findViewById(R.id.progressBar)
        this.fab = this.findViewById(R.id.extended_fab)
    }

    private fun observeData(command: Command) {
        when (command) {
            is Command.DeleteCountItemData -> {
                this.adapter.deleteItem(command.item)
            }
            is Command.RefreshAllCountData -> {
                if (this.cardViewSearchCounter.visibility == View.GONE)
                    this.cardViewSearchCounter.visibility = View.VISIBLE
                if (this.fab.visibility == View.GONE)
                    this.fab.visibility = View.VISIBLE
                if (this.linearLayoutInternetState.visibility == View.VISIBLE)
                    this.linearLayoutInternetState.visibility = View.GONE

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
                this.swipeRefreshLayout.isRefreshing = command.isLoading
            }
            is Command.SearchCounterItemData -> {
                if(command.data.isNotEmpty()){
                    if(this.swipeRefreshLayout.visibility == View.GONE) {
                        this.swipeRefreshLayout.visibility = View.VISIBLE
                    }
                    this.adapter.addItems(command.data)
                    this.textViewSearchResult.visibility = View.GONE
                }else{
                    this.textViewSearchResult.visibility = View.VISIBLE
                    this.swipeRefreshLayout.visibility = View.GONE
                }
            }
        }
    }


    private fun showCommandError(commandError: CommandError) {
        when (commandError) {
            is CommandError.InternetConnection -> {
                if (this.adapter.itemCount == 0) {
                    internetStateView(false)
                } else {
                    Utils.showSimpleErrorDialog(
                        this@CounterListActivity,
                        getString(R.string.generic_error_description),
                        getString(R.string.connection_error_description)
                    )
                }
            }
            is CommandError.SimpleErrorMessage -> {
                Utils.showSimpleErrorDialog(
                    this@CounterListActivity,
                    getString(R.string.generic_error_description),
                    commandError.message
                )
            }
        }

    }

    private fun initRequest() {
        this.counterListViewModel.getAllCounterItems()
    }

    private fun addNewItem() {
        startForResult.launch(Intent(this, CounterAddActivity::class.java))
    }


    private fun progressAction(isShowing: Boolean) {
        this.progressBar.visibility = if (isShowing) View.VISIBLE else View.GONE
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
        this.textViewTotalItems.text = getString(R.string.n_items, itemsCount)
        this.textViewTotalTimes.text = getString(R.string.n_times, timesCount)

    }

    override fun onLongItemClick(
        showActionMode: Boolean,
        view: View,
        counterItem: CountModel?,
        position: Int?
    ) {
        if (showActionMode) {
            this.startSupportActionMode(this)
            this.mMode?.title = getString(R.string.n_selected, 1)
            this.appBarLayout.elevation = 12.0f
        } else {
            this.mMode?.finish()
        }
    }


    private fun emptyStateView(isEmpty: Boolean) {
        this.swipeRefreshLayout.visibility = if (isEmpty) View.GONE else View.VISIBLE
        this.linearLayoutEmptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        this.cardViewSearchCounter.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun internetStateView(isAvailable: Boolean) {
        if (!isAvailable) {
            this.linearLayoutInternetState.visibility = View.VISIBLE
            this.linearLayoutCounterItems.visibility = View.GONE
            this.linearLayoutEmptyState.visibility = View.GONE
            this.cardViewSearchCounter.visibility = View.GONE
            this.swipeRefreshLayout.isRefreshing = false
            this.swipeRefreshLayout.visibility = View.GONE
            this.progressBar.visibility = View.GONE
            this.fab.visibility = View.GONE
        } else {
            this.linearLayoutInternetState.visibility = View.GONE
        }
    }

    private fun isScreenWithOrientationPortrait(): Boolean {
        return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    }

    private fun showSnackbarErrorMessage(message: String?) {
        Snackbar.make(this.fab, message ?: "", Snackbar.LENGTH_LONG).setAction(R.string.ok, null)
            .show()
    }

    private fun searchCounterItemInList() {
        this.editTextSearchInformation.onTextChange {
            counterListViewModel.searchCounterItemByName(it?.toString()!!)
        }
        this.editTextSearchInformation.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                this.appBarLayout.elevation = 0.4f
                this.imageViewRemoveFocus.visibility = View.VISIBLE
            } else {
                this.appBarLayout.elevation = 0.0f
                this.imageViewRemoveFocus.visibility = View.GONE
                this.editTextSearchInformation.hideKeyboard()
            }
        }
        this.imageViewRemoveFocus.setOnClickListener {
            this.editTextSearchInformation.setText("")
            this.editTextSearchInformation.clearFocus()
        }
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
                onDeleteItemCount(
                    this.adapter.getLongItemCounterItemSelected(),
                    this.adapter.getLongItemPositionSelected()
                )
                mode.finish()
                true
            }
            R.id.menu_share_action -> {
                onShareItemCount(
                    this.adapter.getLongItemCounterItemSelected()
                )
                mode.finish()
                true
            }
            else -> false
        }
    }

    private fun onShareItemCount(
        longItemCounterItemSelected: CountModel?
    ) {
        longItemCounterItemSelected?.let { countModel ->
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.n_per_counter_name, countModel.count, countModel.title)
                )
                type = "text/plain"
                startActivity(Intent.createChooser(this, getString(R.string.share)))
            }
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        this.adapter.notifyDataSetChanged()
        this.appBarLayout.elevation = 0.0f
    }

    override fun onActivityResult(result: ActivityResult?) {
        if (result?.resultCode == RESULT_OK) {
            result.data?.extras?.getParcelable<CountModel>(Utils.DATA)?.let {
                this.adapter.addItem(it)
            } ?: run {
                this.counterListViewModel.getAllCounterItems()
            }
        }
    }

}