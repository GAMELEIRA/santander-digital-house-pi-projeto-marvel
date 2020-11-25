package com.example.marvelworld.filters.views

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable

class SuggestAdapter(
    context: Context,
    resource: Int,
    private val suggestions: MutableList<String> = mutableListOf()
) : ArrayAdapter<String>(context, resource, suggestions), Filterable {

    fun setData(suggestions: List<String>) {
        this.suggestions.clear()
        this.suggestions.addAll(suggestions)
    }

    override fun getCount() = suggestions.size

    override fun getItem(position: Int) = suggestions[position]

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    filterResults.values = suggestions
                    filterResults.count = suggestions.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}