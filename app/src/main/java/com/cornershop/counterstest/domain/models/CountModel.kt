package com.cornershop.counterstest.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountModel(
    var id: String = "",
    var title: String = "",
    var count: Int = 0
): Parcelable