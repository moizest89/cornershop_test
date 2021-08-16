package com.cornershop.counterstest.data.repository.count

import com.cornershop.counterstest.data.datasources.remote.RemoteDataSourceImpl
import com.cornershop.counterstest.data.mappers.toCountActionRequest
import com.cornershop.counterstest.data.mappers.toCountModel
import com.cornershop.counterstest.data.mappers.toCounterModelList
import com.cornershop.counterstest.data.models.CounterRequestItem
import com.cornershop.counterstest.data.models.CounterResponseItem
import com.cornershop.counterstest.data.utils.RepositoryResult
import com.cornershop.counterstest.domain.models.CountModel
import com.cornershop.counterstest.domain.repository.counter.CounterRepository
import com.cornershop.counterstest.domain.utils.errorMessage
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class CounterRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl
) : CounterRepository {

    override suspend fun getAllCounterItems(): RepositoryResult<MutableList<CountModel>> {
        try {
            val response = remoteDataSource.getCounters()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return RepositoryResult.Success(body.toCounterModelList())
                }
            }
            return RepositoryResult.Error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return e.errorMessage()
        } catch (e: HttpException) {
            return e.errorMessage()
        }
    }

    override suspend fun addCounterItem(name: String): RepositoryResult<CountModel> {
        try {
            val response = remoteDataSource.addCounterItem(CounterRequestItem(name))
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return RepositoryResult.Success(body.first { it.title == name }
                        .toCountModel())
                }
            }
            return RepositoryResult.Error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return e.errorMessage()
        } catch (e: HttpException) {
            return e.errorMessage()
        }
    }

    override suspend fun deleteCounterItem(countModel: CountModel): RepositoryResult<CountModel> {
        try {
            val response = remoteDataSource.deleteCounter(countModel.toCountActionRequest())
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return RepositoryResult.Success(countModel)
                }
            }
            return RepositoryResult.Error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return e.errorMessage()
        } catch (e: HttpException) {
            return e.errorMessage()
        }
    }

    override suspend fun incrementCounterItem(countModel: CountModel): RepositoryResult<CountModel> =
        actionCounterItem(
            remoteDataSource.incrementCounter(countModel.toCountActionRequest()),
            countModel
        )

    override suspend fun decrementCounterItem(countModel: CountModel): RepositoryResult<CountModel> =
        actionCounterItem(
            remoteDataSource.decrementCounter(countModel.toCountActionRequest()),
            countModel
        )

    private fun actionCounterItem(
        response: Response<MutableList<CounterResponseItem>>,
        countModel: CountModel
    ): RepositoryResult<CountModel> {
        try {
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return RepositoryResult.Success(body.first { it.id == countModel.id }
                        .toCountModel())
                }
            }
            return RepositoryResult.Error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return e.errorMessage()
        } catch (e: HttpException) {
            return e.errorMessage()
        }
    }

}