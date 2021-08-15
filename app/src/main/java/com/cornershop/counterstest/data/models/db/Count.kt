package com.cornershop.counterstest.data.models.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Channel(
    @PrimaryKey var id: String = "",
    var count: Int = 0,
    var title: String = ""
)
