package com.cornershop.counterstest.domain.models

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("email")
    val isFirstTime: Boolean = true
)
