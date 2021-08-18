package com.cornershop.counterstest.data.datasources.db

import com.cornershop.counterstest.data.models.db.CountItem
import javax.inject.Inject

class DataBaseDataSourceImpl @Inject constructor(
    private val counterDao: CounterDao
) : DataBaseDataSource {

    override suspend fun insertAllCountItems(countItems: MutableList<CountItem>): MutableList<CountItem> {
        this.counterDao.deleteCounterItems()
        if(countItems.isNotEmpty()) {
            this.counterDao.insertCounterItems(countItems)
        }
        return countItems
    }

    override suspend fun getAllCountItems(): MutableList<CountItem> {
        val items = this.counterDao.getCounterItems().toMutableList()
        return items
    }

    override suspend fun getAllCountItemsByName(name: String): MutableList<CountItem> {
        return this.counterDao.getCounterItemsByName("%${name}%").toMutableList()
    }

    override suspend fun deleteAllCountItems() {
        this.counterDao.deleteCounterItems()
    }

}