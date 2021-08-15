package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.data.repository.count.CounterRepositoryImpl
import com.cornershop.counterstest.data.utils.RepositoryResult
import com.cornershop.counterstest.domain.models.CountModel
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.CommandError
import javax.inject.Inject

class DeleteCounterItemUseCase @Inject constructor(
    private val counterRepository: CounterRepositoryImpl
) : CounterActionUseCase{
    override suspend fun invoke( countModel: CountModel): Command {
        return when(val result = counterRepository.deleteCounterItem(countModel)){
            is RepositoryResult.Success -> Command.DeleteCountItemData( item = result.data)
            is RepositoryResult.Error -> Command.Error( CommandError.SimpleErrorMessage(result.message))
        }
    }
}