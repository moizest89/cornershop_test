package com.cornershop.counterstest.presentation.counter.add

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.cornershop.counterstest.R
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.CommandError
import com.cornershop.counterstest.presentation.base.BaseActivity
import com.cornershop.counterstest.presentation.counter.examples.CounterExamplesActivity
import com.cornershop.counterstest.presentation.counter.list.CounterListViewModel
import com.cornershop.counterstest.presentation.utils.Utils
import com.cornershop.counterstest.presentation.utils.onTextChange
import com.cornershop.counterstest.presentation.utils.showKeyboard
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CounterAddActivity : BaseActivity(), ActivityResultCallback<ActivityResult> {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var materialButtonSave: MaterialButton
    private lateinit var textViewGetExampleItem: TextView
    private lateinit var textInputLayoutCounterName: TextInputLayout

    private val counterListViewModel by viewModels<CounterListViewModel>()
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counter_add)
        this.inflateViews()
        this.createToolbar()

        this.counterListViewModel.counterData.observe(this) {
            observeData(it)
        }

        this.textInputLayoutCounterName.editText?.onTextChange {
            if (it.isNullOrBlank()) {
                materialButtonSave.isEnabled = false
                materialButtonSave.alpha = 0.3f
            } else {
                materialButtonSave.isEnabled = true
                materialButtonSave.alpha = 1f
            }
        }

        this.textInputLayoutCounterName.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId != 0 || event.action === KeyEvent.ACTION_DOWN) {
                sendData()
                true
            } else {
                false
            }
        }

        this.materialButtonSave.setOnClickListener {
            sendData()
        }

        this.textViewGetExampleItem.setOnClickListener {
            getItemExample()
        }

    }

    private fun sendData() {
        val name = textInputLayoutCounterName.editText?.text.toString().trim()
        if (name.isNotBlank()) {
            this.counterListViewModel.addNewCounterItem(
                textInputLayoutCounterName.editText?.text.toString().trim()
            )
        }
    }

    private fun inflateViews() {
        this.toolbar = this.findViewById(R.id.toolbar)
        this.progressBar = this.findViewById(R.id.progressBar)
        this.materialButtonSave = this.findViewById(R.id.materialButtonSave)
        this.textViewGetExampleItem = this.findViewById(R.id.textViewGetExampleItem)
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
                val bundle = Bundle()
                bundle.putParcelable(Utils.DATA, command.item)
                val intent = Intent()
                intent.putExtras(bundle)
                setResult(RESULT_OK, intent)
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

    private fun getItemExample() {
        startForResult.launch(Intent(this, CounterExamplesActivity::class.java))
    }

    override fun onActivityResult(result: ActivityResult?) {
        if (result?.resultCode == RESULT_OK) {
            result.data?.extras?.getString(Utils.DATA)?.let {
                textInputLayoutCounterName.editText?.let { editText ->
                    editText.setText(it)
                    editText.setSelection(it.length)
                    this.showKeyboard(editText)
                }
            }
        }
    }

}