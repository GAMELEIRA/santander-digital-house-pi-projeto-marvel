package com.example.marvelworld.filters.models

class Filter(
    val text: String? = null,
    val filterMap: HashMap<Int, List<FilterServiceResponse>> = hashMapOf(
        CHARACTER to listOf(),
        COMIC to listOf(),
        EVENT to listOf(),
        SERIES to listOf(),
        CREATOR to listOf()
    )
) {

    fun isEmpty(): Boolean {
        var result = true
        result = result && text.isNullOrBlank()

        for ((_, v) in filterMap) {
            if(!result) break

            result = result && v.isEmpty()
        }

        return result
    }

    companion object {
        const val CHARACTER: Int = 0
        const val COMIC: Int = 1
        const val EVENT: Int = 2
        const val SERIES: Int = 3
        const val CREATOR: Int = 4
    }
}