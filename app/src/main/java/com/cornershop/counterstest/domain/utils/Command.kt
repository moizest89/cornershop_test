package com.cornershop.counterstest.domain.utils

import com.cornershop.counterstest.domain.models.CountModel

sealed class Command {
    data class RefreshAllCountData(val data: MutableList<CountModel>) : Command()
    data class SearchCounterItemData(val data: MutableList<CountModel>) : Command()
    data class AddOrUpdateCountItemData(val item: CountModel?) : Command()
    data class DeleteCountItemData(val item: CountModel?) : Command()
    data class Loading(var isLoading: Boolean) : Command()
    data class Error(val error: CommandError) : Command()
}

sealed class CommandError {
    object InternetConnection : CommandError()
    data class SimpleErrorMessage( val message: String? ="") : CommandError()
}