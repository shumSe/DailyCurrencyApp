package ru.shumikhin.dailycurrencyapp.data.repository.utils

import java.io.IOException

sealed class RequestResult<out E : Any> {
    data object Loading : RequestResult<Nothing>()
    class Success<out E : Any>(val data: E) : RequestResult<E>()
    class Error(val error: Throwable) : RequestResult<Nothing>()
}
fun <T : Any> Result<T>.toRequestResult(): RequestResult<T> {
    return when {
        isSuccess -> RequestResult.Success(data = this.getOrThrow())
        isFailure -> RequestResult.Error(error = this.exceptionOrNull() ?: IOException())
        else -> error("Impossible branch")
    }
}

fun <I : Any, O : Any> RequestResult<I>.map(mapper: (I) -> O): RequestResult<O> {
    return when (this) {
        is RequestResult.Success -> RequestResult.Success(mapper(data))
        is RequestResult.Error -> RequestResult.Error(error)
        is RequestResult.Loading -> RequestResult.Loading
    }
}


