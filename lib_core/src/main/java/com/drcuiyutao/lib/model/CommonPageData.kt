package com.drcuiyutao.lib.model

import com.google.gson.annotations.SerializedName

data class CommonPageData<T>(val pageSize: Int) {
    val pageNumber: Int = 1
    val total: Int = 0
    val totalPages: Int = 0
    @SerializedName(value = "hasNext", alternate = ["hasnext"])
    val hasNext: Boolean = false
    val content: List<T>? = null
}