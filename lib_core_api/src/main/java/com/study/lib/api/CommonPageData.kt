package com.study.lib.api

/**
 * Created by john on 2017/8/29.
 */
open class CommonPageData<T> {
    val content: List<T>? = null
    private val hasnext = false
    val pageNumber = 0
    val pageSize = 0
    val total = 0
    val totalPages = 0
    private val hasNext = false

    operator fun hasNext(): Boolean {
        return hasnext || hasNext
    }

}