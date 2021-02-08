package com.example.marvelworld.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.marvelworld.R

class LoadingDialog(
    private val context: Context,
    private val inflater: LayoutInflater,
    private val text: String
) {
    private lateinit var dialog: AlertDialog

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(context)
        val view = inflater.inflate(R.layout.loading_dialog, ConstraintLayout(context))

        val textView = view.findViewById<TextView>(R.id.loading_text)
        textView.text = text

        builder.setView(view)
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}