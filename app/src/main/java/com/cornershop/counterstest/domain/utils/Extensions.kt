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

fun <T> Exception.errorMessage(): RepositoryResult.Error<T> {
    return try {
        if (this.message == Utils.INTERNET_CONNECTION_MESSAGE) {
            RepositoryResult.Error(
                message = Utils.INTERNET_CONNECTION_MESSAGE,
                code = Utils.INTERNET_CONNECTION_CODE
            )
        } else {
            RepositoryResult.Error(this.message ?: this.toString())
        }
    } catch (e: Exception) {
        RepositoryResult.Error<T>(this.message ?: e.toString())
    }
}


fun <T> RepositoryResult<T>.toCommandError(): Command.Error {
    this.code?.let {
        if (it == Utils.INTERNET_CONNECTION_CODE) {
            return Command.Error(CommandError.InternetConnection)
        }
    }
    return Command.Error(CommandError.SimpleErrorMessage(this.message))
}
