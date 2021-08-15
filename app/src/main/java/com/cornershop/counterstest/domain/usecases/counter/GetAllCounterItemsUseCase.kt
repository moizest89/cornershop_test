package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.repository.count.CounterRepositoryImpl
import com.cornershop.counterstest.data.utils.RepositoryResult
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.CommandError
import javax.inject.Inject

class GetAllCounterItemsUseCase @Inject constructor(
    private val counterRepository: CounterRepositoryImpl
) {

    suspend fun invoke() : Command{
        return when(val result = counterRepository.getAllCounterItems()){
            is RepositoryResult.Success -> Command.RefreshAllCountData( data = result.data ?: mutableListOf() )
            is RepositoryResult.Error -> Command.Error( CommandError.SimpleErrorMessage(result.message))
        }
    }
}