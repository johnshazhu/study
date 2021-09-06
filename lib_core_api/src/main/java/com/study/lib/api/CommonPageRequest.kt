package com.study.lib.api

open class CommonPageRequest<T : BaseResponseData>(private var pageNumber: Int, private val pageSize: Int) : APIBaseRequest<T>() {
    fun setPageNumber(pageNumber: Int) {
        this.pageNumber = pageNumber
    }
    override var url: String
        get() = ""
        set(value) {
            super.url = value
        }
}