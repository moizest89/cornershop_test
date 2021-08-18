package com.cornershop.counterstest.data.repository.count

import com.cornershop.counterstest.data.datasources.db.DataBaseDataSourceImpl
import com.cornershop.counterstest.data.datasources.remote.RemoteDataSourceImpl
import com.cornershop.counterstest.data.mappers.toCountActionRequest
import com.cornershop.counterstest.data.mappers.toCountItemList
import com.cornershop.counterstest.data.mappers.toCountModel
import com.cornershop.counterstest.data.mappers.toCounterModelList
import com.cornershop.counterstest.data.models.CounterRequestItem
import com.cornershop.counterstest.data.utils.RepositoryResult
import com.cornershop.counterstest.domain.models.CountModel
import com.cornershop.counterstest.domain.repository.counter.CounterRepository
import com.cornershop.counterstest.domain.utils.errorMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CounterRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
    private val dataBaseDataSource: DataBaseDataSourceImpl
) : CounterRepository {

    override suspend fun getAllCounterItems(): Flow<RepositoryResult<MutableList<CountModel>>> {
        return flow {
            //Get data from database
            emit(RepositoryResult.Loading())
            val fromDataBase = dataBaseDataSource.getAllCountItems().toCountModel()
            emit(RepositoryResult.Success(data = fromDataBase))

            //Get data from api service
            emit(RepositoryResult.Loading())
            val fromApiService = fetchCounterItems()
            emit(fromApiService)
            emit(RepositoryResult.Loading(false))
        }
    }

    private suspend fun fetchCounterItems(): RepositoryResult<MutableList<CountModel>> {
        try {
            val response = remoteDataSource.getCounters()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    this.dataBaseDataSource.insertAllCountItems(body.toCountItemList())
                    return RepositoryResult.Success(body.toCounterModelList())
                }
            }
            return RepositoryResult.Error("${response.code()} ${response.message()}")
        } catch (e: IOException) {
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
                    this.dataBaseDataSource.insertAllCountItems(body.toCountItemList())
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
                    this.dataBaseDataSource.insertAllCountItems(body.toCountItemList())
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

    override suspend fun incrementCounterItem(countModel: CountModel): RepositoryResult<CountModel> {
        try {
            val response = remoteDataSource.incrementCounter(countModel.toCountActionRequest())
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    this.dataBaseDataSource.insertAllCountItems(body.toCountItemList())
                    return RepositoryResult.Success(body.first { it.id == countModel.id }
                        .toCountModel())
                }
            }
            return RepositoryResult.Error("${response.code()} ${response.message()}")
        } catch (e: IOException) {
            return e.errorMessage()
        } catch (e: HttpException) {
            return e.errorMessage()
        }
    }

    override suspend fun decrementCounterItem(countModel: CountModel): RepositoryResult<CountModel> {
        try {
            val response = remoteDataSource.decrementCounter(countModel.toCountActionRequest())
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    this.dataBaseDataSource.insertAllCountItems(body.toCountItemList())
                    return RepositoryResult.Success(body.first { it.id == countModel.id }
                        .toCountModel())
                }
            }
            return RepositoryResult.Error("${response.code()} ${response.message()}")
        } catch (e: IOException) {
            return e.errorMessage()
        } catch (e: HttpException) {
            return e.errorMessage()
        }
    }

    override suspend fun searchCountItemsByName(name: String): RepositoryResult<MutableList<CountModel>> {
        return if (name.isBlank()) {
            RepositoryResult.Success(dataBaseDataSource.getAllCountItems().toCountModel())
        } else {
            RepositoryResult.Success(dataBaseDataSource.getAllCountItemsByName(name).toCountModel())
        }
    }

}