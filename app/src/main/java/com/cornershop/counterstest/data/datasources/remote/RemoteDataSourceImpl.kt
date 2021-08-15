package com.cornershop.counterstest.data.datasources.remote

import com.cornershop.counterstest.data.models.CounterActionRequestItem
import com.cornershop.counterstest.data.models.CounterRequestItem
import com.cornershop.counterstest.data.models.CounterResponseItem
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor (private var remoteDataService: RemoteDataService) : RemoteDataSource {
    override suspend fun getCounters(): Response<MutableList<CounterResponseItem>> =
        remoteDataService.getCounters()

    override suspend fun addCounterItem(counterRequestItem: CounterRequestItem): Response<MutableList<CounterResponseItem>> =
        remoteDataService.addCounter(counterRequestItem)

    override suspend fun deleteCounter(counterActionRequestItem: CounterActionRequestItem): Response<MutableList<CounterResponseItem>> =
        remoteDataService.deleteCounter(counterActionRequestItem)

    override suspend fun incrementCounter(counterActionRequestItem: CounterActionRequestItem): Response<MutableList<CounterResponseItem>> =
        remoteDataService.incrementCounter(counterActionRequestItem)

    override suspend fun decrementCounter(counterActionRequestItem: CounterActionRequestItem): Response<MutableList<CounterResponseItem>> =
        remoteDataService.decrementCounter(counterActionRequestItem)
}