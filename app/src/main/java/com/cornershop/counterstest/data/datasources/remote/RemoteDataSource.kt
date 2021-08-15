package com.cornershop.counterstest.data.datasources.remote

import com.cornershop.counterstest.data.models.CounterActionRequestItem
import com.cornershop.counterstest.data.models.CounterRequestItem
import com.cornershop.counterstest.data.models.CounterResponseItem
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getCounters(): Response<MutableList<CounterResponseItem>>
    suspend fun addCounterItem(counterRequestItem: CounterRequestItem): Response<MutableList<CounterResponseItem>>
    suspend fun deleteCounter(counterActionRequestItem: CounterActionRequestItem): Response<MutableList<CounterResponseItem>>
    suspend fun incrementCounter(counterActionRequestItem: CounterActionRequestItem): Response<MutableList<CounterResponseItem>>
    suspend fun decrementCounter(counterActionRequestItem: CounterActionRequestItem): Response<MutableList<CounterResponseItem>>
}