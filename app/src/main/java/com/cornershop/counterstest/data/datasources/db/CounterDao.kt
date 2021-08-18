package com.cornershop.counterstest.data.datasources.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cornershop.counterstest.data.models.db.CountItem

@Dao
interface CounterDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCounterItems(counterItems: List<CountItem>)

    @Query("SELECT * FROM CountItem")
    suspend fun getCounterItems(): Array<CountItem>

    @Query("SELECT * FROM CountItem WHERE title LIKE :name")
    suspend fun getCounterItemsByName(name: String): List<CountItem>

    @Query("DELETE FROM CountItem")
    suspend fun deleteCounterItems()
}