package com.cornershop.counterstest.domain.utils

import com.cornershop.counterstest.data.utils.RepositoryResult
import retrofit2.HttpException

fun <T> HttpException.errorMessage(): RepositoryResult.Error<T> {
    return try {
        this.response()?.errorBody()?.byteString()?.utf8()?.also {
            RepositoryResult.Error<T>(this.message ?: it)
        }
    } catch (e: Exception) {
        RepositoryResult.Error<T>(this.message ?: e.toString())
    } as RepositoryResult.Error<T>
}
