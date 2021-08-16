package com.cornershop.counterstest.presentation.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.cornershop.counterstest.R

object Utils {

    const val DATA = "sendData"

    fun showSimpleErrorDialog(
        context: Context,
        title: String,
        message: String?,
        onPositiveAction: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.ok) { _, _ ->
                onPositiveAction?.invoke()
            }
            .setCancelable(false)
            .show()
    }



}