package com.example.marvelworld.detailcard.views

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.marvelworld.R
import com.example.marvelworld.api.models.Url
import java.util.*

class UrlsAdapter(
    context: Context,
    urls: List<Url>,
    private val onUrlClickListener: OnUrlClickListener
) : ArrayAdapter<Url>(context, 0, urls) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val url: Url? = getItem(position)
        val viewHolder: ViewHolder
        val view: View?

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.url_item, parent, false)
            viewHolder = ViewHolder(view, onUrlClickListener)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        viewHolder.bind(url, position)

        return view as View
    }

    class ViewHolder(
        view: View?,
        private val onUrlClickListener: OnUrlClickListener
    ) {
        private val urlType: TextView? = view?.findViewById(R.id.url_type)

        fun bind(url: Url?, position: Int) {
            urlType?.text = url?.type?.toUpperCase(Locale.ROOT)
            urlType?.paintFlags = urlType?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!

            urlType.setOnClickListener {
                onUrlClickListener.onUrlClick(position)
            }
        }
    }
}