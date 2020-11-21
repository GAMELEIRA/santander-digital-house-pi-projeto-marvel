package com.example.marvelworld.reusablecomponents.urlsdialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import com.example.marvelworld.R
import com.example.marvelworld.api.models.Url
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class UrlsBottomDialogFragment(private val urls: List<Url>) : BottomSheetDialogFragment(),
    OnUrlClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.urls_dialog, container, false)

        val closeButton = view.findViewById<ImageButton>(R.id.close_button)
        closeButton.setOnClickListener {
            this.dismiss()
        }

        val listView = view.findViewById<ListView>(R.id.url_list)
        val adapter = UrlsAdapter(view.context, urls, this)

        listView.adapter = adapter

        return view
    }

    override fun onUrlClick(position: Int) {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse(urls[position].url)
            startActivity(intent)
    }
}