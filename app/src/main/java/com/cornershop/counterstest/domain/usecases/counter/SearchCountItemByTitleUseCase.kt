package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.repository.count.CounterRepositoryImpl
import com.cornershop.counterstest.data.utils.RepositoryResult
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.toCommandError
import javax.inject.Inject

class SearchCountItemByTitleUseCase @Inject constructor(
    private val counterRepository: CounterRepositoryImpl
) {
    suspend fun invoke( name : String) : Command {
        return when(val result = counterRepository.searchCountItemsByName(name)){
            is RepositoryResult.Success -> Command.SearchCounterItemData(data = result.data!!)
            is RepositoryResult.Error -> result.toCommandError()
            is RepositoryResult.Loading -> Command.Loading(result.isLoading)
        }
    }
}