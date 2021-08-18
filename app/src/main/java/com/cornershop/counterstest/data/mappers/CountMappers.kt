package com.cornershop.counterstest.data.mappers

import com.cornershop.counterstest.data.models.CounterActionRequestItem
import com.cornershop.counterstest.data.models.CounterRequestItem
import com.cornershop.counterstest.data.models.CounterResponseItem
import com.cornershop.counterstest.data.models.db.CountItem
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

fun MutableList<CounterResponseItem>.toCountItemList(): MutableList<CountItem> {
    return this.map { it.toCountItem() }.toMutableList()
}

fun CounterResponseItem.toCountItem(): CountItem {
    return CountItem(
        id = this.id,
        count = this.count,
        title = this.title
    )
}

fun MutableList<CountItem>.toCountModel() : MutableList<CountModel>{
    return this.map { it.toCountModel() }.toMutableList()
}

fun CountItem.toCountModel() : CountModel{
    return CountModel(
        id = this.id,
        title = this.title,
        count = this.count
    )
}