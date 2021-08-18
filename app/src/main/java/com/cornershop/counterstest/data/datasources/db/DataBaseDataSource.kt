package com.cornershop.counterstest.data.datasources.db

import com.cornershop.counterstest.data.models.db.CountItem

interface DataBaseDataSource {
    suspend fun insertAllCountItems(countItems: MutableList<CountItem>): MutableList<CountItem>
    suspend fun getAllCountItems(): MutableList<CountItem>
    suspend fun getAllCountItemsByName(name: String): MutableList<CountItem>
    suspend fun deleteAllCountItems()
}