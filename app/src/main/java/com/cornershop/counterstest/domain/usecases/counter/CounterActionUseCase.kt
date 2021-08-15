package com.cornershop.counterstest.domain.usecases.counter

import com.cornershop.counterstest.domain.models.CountModel
import com.cornershop.counterstest.domain.utils.Command

interface CounterActionUseCase {
    suspend fun invoke( countModel: CountModel): Command
}