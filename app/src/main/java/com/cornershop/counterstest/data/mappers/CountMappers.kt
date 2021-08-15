package com.cornershop.counterstest.data.mappers

import com.cornershop.counterstest.data.models.CounterActionRequestItem
import com.cornershop.counterstest.data.models.CounterRequestItem
import com.cornershop.counterstest.data.models.CounterResponseItem
import com.cornershop.counterstest.domain.models.CountModel

fun MutableList<CounterResponseItem>.toCounterModelList(): MutableList<CountModel> {
    return this.map { it.toCountModel() }.toMutableList()
}

fun CounterResponseItem.toCountModel(): CountModel {
    return CountModel(
        id = this.id,
        title = this.title,
        count = this.count
    )
}

fun CountModel.toCountRequest(): CounterRequestItem {
    return CounterRequestItem(
        title = this.title
    )
}

fun CountModel.toCountActionRequest(): CounterActionRequestItem {
    return CounterActionRequestItem(
        id = this.id
    )
}