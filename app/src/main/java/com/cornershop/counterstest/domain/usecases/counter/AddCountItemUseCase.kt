package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.repository.count.CounterRepositoryImpl
import com.cornershop.counterstest.data.utils.RepositoryResult
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.CommandError
import javax.inject.Inject

class AddCountItemUseCase @Inject constructor(
    private val counterRepository: CounterRepositoryImpl
) {

    suspend fun invoke( name : String) : Command {
        return when(val result = counterRepository.addCounterItem(name)){
            is RepositoryResult.Success -> Command.AddOrUpdateCountItemData(item = result.data)
            is RepositoryResult.Error -> Command.Error( CommandError.SimpleErrorMessage(result.message))
        }
    }
}