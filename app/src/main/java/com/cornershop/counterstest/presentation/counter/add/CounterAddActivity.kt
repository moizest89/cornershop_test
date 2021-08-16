package com.cornershop.counterstest.presentation.counter.add

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.CommandError
import com.cornershop.counterstest.presentation.counter.list.CounterListViewModel
import com.cornershop.counterstest.presentation.utils.Utils
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CounterAddActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var materialButtonSave: MaterialButton
    private lateinit var textInputLayoutCounterName: TextInputLayout

    private val counterListViewModel by viewModels<CounterListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter_add)
        this.inflateViews()
        this.createToolbar()

        this.counterListViewModel.counterData.observe(this) {
            observeData(it)
        }

        this.materialButtonSave.setOnClickListener {
            this.counterListViewModel.addNewCounterItem(
                textInputLayoutCounterName.editText?.text.toString().trim()
            )
        }
    }

    private fun inflateViews() {
        this.toolbar = this.findViewById(R.id.toolbar)
        this.progressBar = this.findViewById(R.id.progressBar)
        this.materialButtonSave = this.findViewById(R.id.materialButtonSave)
        this.textInputLayoutCounterName = this.findViewById(R.id.textInputLayoutCounterName)
    }

    private fun createToolbar() {
        toolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_close)
        toolbar.title = getString(R.string.create_counter)
        setSupportActionBar(toolbar)
    }

    private fun observeData(command: Command) {
        when (command) {
            is Command.AddOrUpdateCountItemData -> {
                setResult(RESULT_OK, Intent())
                finish()
            }
            is Command.Error -> {
                when (command.error) {
                    CommandError.InternetConnection -> {
                        Utils.showSimpleErrorDialog(
                            this@CounterAddActivity,
                            getString(R.string.error_creating_counter_title),
                            getString(R.string.connection_error_description)
                        )
                    }
                    is CommandError.SimpleErrorMessage -> {
                        Utils.showSimpleErrorDialog(
                            this@CounterAddActivity,
                            getString(R.string.generic_error_description),
                            command.error.message
                        )
                    }
                }
                loadingStatus(false)
            }
            is Command.Loading -> {
                loadingStatus(command.isLoading)
            }
            else -> {
            }
        }
    }

    private fun loadingStatus(isLoading: Boolean) {
        this.materialButtonSave.visibility =
            if (isLoading) View.GONE else View.VISIBLE
        this.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}