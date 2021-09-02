package com.cornershop.counterstest.domain.repository.counter

import com.cornershop.counterstest.data.utils.RepositoryResult
import com.cornershop.counterstest.domain.models.CountModel
import kotlinx.coroutines.flow.Flow

interface CounterRepository {
    suspend fun getAllCounterItems(): Flow<RepositoryResult<MutableList<CountModel>>>
    suspend fun addCounterItem(name: String): RepositoryResult<CountModel>
    suspend fun deleteCounterItem(countModel: CountModel): RepositoryResult<CountModel>
    suspend fun deleteCounterItems(countModel: MutableList<CountModel>): Flow<RepositoryResult<MutableList<CountModel>>>
    suspend fun incrementCounterItem(countModel: CountModel): RepositoryResult<CountModel>
    suspend fun decrementCounterItem(countModel: CountModel): RepositoryResult<CountModel>
    suspend fun searchCountItemsByName(name: String): RepositoryResult<MutableList<CountModel>>
}