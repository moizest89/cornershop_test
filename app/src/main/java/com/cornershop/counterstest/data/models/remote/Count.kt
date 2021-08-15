package com.cornershop.counterstest.data.models

import com.google.gson.annotations.SerializedName

data class CounterResponseItem(
    @SerializedName("count")
    var count: Int = 0,
    @SerializedName("id")
    var id: String = "",
    @SerializedName("title")
    var title: String = ""
)

data class CounterRequestItem(
    @SerializedName("title")
    var title: String? = ""
)

data class CounterActionRequestItem(
    @SerializedName("id")
    var id: String = ""
)