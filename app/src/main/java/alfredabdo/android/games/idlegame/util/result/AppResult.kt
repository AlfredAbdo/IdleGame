package alfredabdo.android.games.idlegame.util.result

data class AppResult<out T>(
    private val value: Any?,
    val additionalData: Map<Any, Any?>,
) {

    companion object {
        fun <T> loading(additionalData: Map<Any, Any?> = emptyMap()) = AppResult<T>(Loading, additionalData)
        fun <T> success(value: T, additionalData: Map<Any, Any?> = emptyMap()) = AppResult<T>(value, additionalData)
        fun <T> error(exception: Throwable, additionalData: Map<Any, Any?> = emptyMap()) =
            AppResult<T>(Error(exception), additionalData)
    }


    private data object Loading {
        override fun toString(): String = "Loading"
    }

    private data class Error(
        val exception: Throwable,
    ) {
        override fun equals(other: Any?): Boolean = other is Error && exception == other.exception
        override fun hashCode(): Int = exception.hashCode()
        override fun toString(): String = "Error($exception)"
    }


    val isLoading: Boolean get() = value is Loading
    val isSuccess: Boolean get() = value !is Loading && value !is Error
    val isError: Boolean get() = value is Error

    @Suppress("UNCHECKED_CAST")
    fun get(): T = value as T

    @Suppress("UNCHECKED_CAST")
    fun getOrNull(): T? =
        when {
            isSuccess -> value as T
            else -> null
        }

    fun exception(): Throwable = (value as Error).exception

    fun exceptionOrNull(): Throwable? =
        when (value) {
            is Error -> value.exception
            else -> null
        }

    override fun toString(): String =
        when (value) {
            isSuccess -> "Success($value)"
            else -> value.toString()
        }
}


/*
inline fun <R> runCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

inline fun <T, R> T.runCatching(block: T.() -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        Result.failure(e)
    }
}

inline fun <T> Result<T>.getOrThrow(): T {
    if (value is AppResult.Error) throw value.exception
    return value as T
}

inline fun <R, T : R> Result<T>.getOrElse(onFailure: (exception: Throwable) -> R): R {
    return when (val exception = exceptionOrNull()) {
        null -> value as T
        else -> onFailure(exception)
    }
}

inline fun <R, T : R> Result<T>.getOrDefault(defaultValue: R): R {
    if (isFailure) return defaultValue
    return value as T
}*/

inline fun <T> AppResult<T>.fold(
    onLoading: AppResult<T>.() -> Unit = {},
    onSuccess: AppResult<T>.(value: T) -> Unit,
    onError: AppResult<T>.(exception: Throwable) -> Unit,
) {
    return when {
        isLoading -> onLoading()
        isError -> onError(exception())
        else -> onSuccess(get())
    }
}

/*inline fun <R, T> Result<T>.map(transform: (value: T) -> R): Result<R> {
    return when {
        isSuccess -> Result.success(transform(value as T))
        else -> Result(value)
    }
}

inline fun <R, T> Result<T>.mapCatching(transform: (value: T) -> R): Result<R> {
    return when {
        isSuccess -> runCatching { transform(value as T) }
        else -> Result(value)
    }
}

inline fun <R, T : R> Result<T>.recover(transform: (exception: Throwable) -> R): Result<R> {
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> Result.success(transform(exception))
    }
}

inline fun <R, T : R> Result<T>.recoverCatching(transform: (exception: Throwable) -> R): Result<R> {
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> runCatching { transform(exception) }
    }
}

inline fun <T> Result<T>.onFailure(action: (exception: Throwable) -> Unit): Result<T> {
    exceptionOrNull()?.let { action(it) }
    return this
}

inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> {
    if (isSuccess) action(value as T)
    return this
}*/
