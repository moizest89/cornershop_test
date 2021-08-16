package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.repository.count.CounterRepositoryImpl
import com.cornershop.counterstest.data.utils.RepositoryResult
import com.cornershop.counterstest.domain.models.CountModel
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.CommandError
import com.cornershop.counterstest.domain.utils.toCommandError
import javax.inject.Inject

class IncrementCountByItemUseCase @Inject constructor(
    private val counterRepository: CounterRepositoryImpl
)  : CounterActionUseCase {

    override suspend fun invoke(countModel: CountModel): Command {
        return when(val result = counterRepository.incrementCounterItem(countModel)){
            is RepositoryResult.Success -> Command.AddOrUpdateCountItemData(item = result.data)
            is RepositoryResult.Error -> result.toCommandError()
        }
    }
}