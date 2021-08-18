package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.repository.count.CounterRepositoryImpl
import com.cornershop.counterstest.data.utils.RepositoryResult
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.toCommandError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllCounterItemsUseCase @Inject constructor(
    private val counterRepository: CounterRepositoryImpl
) {

    suspend fun invoke(): Flow<Command> = counterRepository.getAllCounterItems().map {
        when (it) {
            is RepositoryResult.Success -> Command.RefreshAllCountData(
                data = it.data ?: mutableListOf()
            )
            is RepositoryResult.Error -> it.toCommandError()
            is RepositoryResult.Loading -> Command.Loading(it.isLoading)
        }
    }.flowOn(Dispatchers.IO)
}