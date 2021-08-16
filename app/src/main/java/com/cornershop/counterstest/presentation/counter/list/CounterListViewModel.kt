package com.cornershop.counterstest.presentation.counter.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.domain.models.CountModel
import com.cornershop.counterstest.domain.usecases.counter.*
import com.cornershop.counterstest.domain.utils.Command
import com.cornershop.counterstest.domain.utils.CommandError
import com.cornershop.counterstest.presentation.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterListViewModel @Inject constructor(
    private val networkUtils: NetworkUtils,
    private val getAllCounterItemsUseCase: GetAllCounterItemsUseCase,
    private val addCountItemUseCase: AddCountItemUseCase,
    private val deleteCounterItemUseCase: DeleteCounterItemUseCase,
    private val incrementCountByItemUseCase: IncrementCountByItemUseCase,
    private val decrementCountByItemUseCase: DecrementCountByItemUseCase
) : ViewModel() {

    private val _counterData: MutableLiveData<Command> = MutableLiveData()
    val counterData: MutableLiveData<Command> = _counterData

    fun addNewCounterItem(name: String) {
        viewModelScope.launch {
            counterData.value = Command.Loading(isLoading = true)
            if(networkUtils.hasInternetConnection()){
                counterData.value = addCountItemUseCase.invoke(name)
            }else{
                counterData.value = Command.Error(error = CommandError.InternetConnection)
            }
            counterData.value = Command.Loading(isLoading = false)
        }
    }

    fun getAllCounterItems() {
        viewModelScope.launch {
            counterData.value = Command.Loading(isLoading = true)
            if(networkUtils.hasInternetConnection()) {
                counterData.value = getAllCounterItemsUseCase.invoke()
            }else{
                counterData.value = Command.Error(error = CommandError.InternetConnection)
            }
            counterData.value = Command.Loading(isLoading = false)
        }
    }

    fun incrementCounterItem(countModel: CountModel, position: Int) {
        viewModelScope.launch {
            counterData.value = Command.Loading(isLoading = true)
            if(networkUtils.hasInternetConnection()) {
                counterData.value = incrementCountByItemUseCase.invoke(countModel)
            }else{
                counterData.value = Command.Error(error = CommandError.InternetConnection)
            }
            counterData.value = Command.Loading(isLoading = false)
        }
    }

    fun decrementCounterItem(countModel: CountModel, position: Int) {
        if (countModel.count > 0) {
            viewModelScope.launch {
                counterData.value = Command.Loading(isLoading = true)
                if(networkUtils.hasInternetConnection()) {
                    counterData.value = decrementCountByItemUseCase.invoke(countModel)
                }else{
                    counterData.value = Command.Error(error = CommandError.InternetConnection)
                }
                counterData.value = Command.Loading(isLoading = false)
            }
        }
    }

    fun deleteItemCounterItem(countModel: CountModel, position: Int) {
        viewModelScope.launch {
            counterData.value = Command.Loading(isLoading = true)
            counterData.value = deleteCounterItemUseCase.invoke(countModel)
            counterData.value = Command.Loading(isLoading = false)
        }
    }

}