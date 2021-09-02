package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.utils.RepositoryResult
import com.cornershop.counterstest.domain.models.CountModel
import com.cornershop.counterstest.domain.repository.counter.CounterRepository
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.toCommandError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeleteCounterItemsUseCase @Inject constructor(
    private val counterRepository: CounterRepository
) {

    suspend fun invoke(countItems: MutableList<CountModel>): Flow<Command> =
        counterRepository.deleteCounterItems(countItems).map {
            when (it) {
                is RepositoryResult.Success -> Command.RefreshAllCountData(
                    data = it.data ?: mutableListOf()
                )
                is RepositoryResult.Error -> it.toCommandError()
                is RepositoryResult.Loading -> Command.Loading(it.isLoading)
            }
        }.flowOn(Dispatchers.IO)
}