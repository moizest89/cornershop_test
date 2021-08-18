package com.cornershop.counterstest.data.datasources.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cornershop.counterstest.data.models.db.CountItem

@Database(entities = [CountItem::class], version = 1)
abstract class CounterDataBase : RoomDatabase() {
    abstract fun counterDao(): CounterDao
}