package com.cornershop.counterstest.data.datasources.remote

import com.cornershop.counterstest.data.models.CounterActionRequestItem
import com.cornershop.counterstest.data.models.CounterRequestItem
import com.cornershop.counterstest.data.models.CounterResponseItem
import retrofit2.Response
import retrofit2.http.*


interface RemoteDataService {

    @GET("counters")
    suspend fun getCounters(): Response<MutableList<CounterResponseItem>>

    @POST("counter")
    suspend fun addCounter(@Body counterRequestItem: CounterRequestItem): Response<MutableList<CounterResponseItem>>

//    @DELETE("counter")
    @HTTP(method = "DELETE", path = "counter", hasBody = true)
    suspend fun deleteCounter(@Body counterActionRequestItem: CounterActionRequestItem): Response<MutableList<CounterResponseItem>>

    @POST("counter/inc")
    suspend fun incrementCounter(@Body counterActionRequestItem: CounterActionRequestItem): Response<MutableList<CounterResponseItem>>

    @POST("counter/dec")
    suspend fun decrementCounter(@Body counterActionRequestItem: CounterActionRequestItem): Response<MutableList<CounterResponseItem>>
}