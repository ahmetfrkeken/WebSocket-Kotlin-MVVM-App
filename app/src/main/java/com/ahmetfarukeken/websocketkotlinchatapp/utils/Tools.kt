package com.ahmetfarukeken.websocketkotlinchatapp.utils

import android.app.AlertDialog
import android.content.Context
import com.ahmetfarukeken.websocketkotlinchatapp.R

class Tools {
    companion object {
        fun showNoInternetDialog(context: Context) {
            AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.there_is_no_internet_connection))
                .setMessage(context.getString(R.string.please_check_your_network_connection))
                .setPositiveButton(context.getString(R.string.okay)) { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }
}