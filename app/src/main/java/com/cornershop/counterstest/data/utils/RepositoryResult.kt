package com.cornershop.counterstest.data.utils

sealed class RepositoryResult<T>(
    val data: T? = null,
    val message: String? = null,
    val code: Int? = -1
) {
    class Success<T>(data: T) : RepositoryResult<T>(data)
    class Error<T>(message: String, data: T? = null, code: Int? = null) : RepositoryResult<T>(data, message, code)
    data class Loading<T>( var isLoading : Boolean = true) : RepositoryResult<T>()
}