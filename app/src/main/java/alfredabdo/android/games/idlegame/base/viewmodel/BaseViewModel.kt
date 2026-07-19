package alfredabdo.android.games.idlegame.base.viewmodel

import alfredabdo.android.games.idlegame.base.UseCase
import alfredabdo.android.games.idlegame.base.inject.Injections
import alfredabdo.android.games.idlegame.data.exception.AppException
import alfredabdo.android.games.idlegame.ui.state.UIState
import alfredabdo.android.games.idlegame.util.result.AppResult
import alfredabdo.android.games.idlegame.util.result.fold
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

abstract class BaseViewModel : ViewModel() {

    protected val exceptionConverter get() = Injections.exceptionConverter


    //TODO change execute terminology to reflect creating a cold flow instead


    //...

    protected fun <I, O> useCaseFlow(useCase: UseCase<I, O>, input: I): Flow<AppResult<O>> {
        return flow {
            emit(AppResult.Companion.loading())
            try {
                val data = useCase(input)
                emit(AppResult.success(data))
            } catch (e: Throwable) {
                emit(AppResult.Companion.error(e))
            }
        }
    }

    protected fun <O> useCaseFlow(useCase: UseCase<Unit, O>): Flow<AppResult<O>> =
        useCaseFlow(useCase, Unit)


    protected inline fun <R> runCoroutineCatching(block: () -> R): Result<R> {
        return try {
            Result.success(block())
        } catch (e: Throwable) {
            if (e is CancellationException) throw e
            Result.failure(e)
        }
    }

    protected inline fun <T> AppResult<T>.handleResult(
        onLoading: AppResult<T>.() -> Unit = {},
        onError: AppResult<T>.(exception: AppException) -> Unit = {},
        onSuccess: AppResult<T>.(value: T) -> Unit = {},
    ) {
        fold(
            onLoading = onLoading,
            onSuccess = onSuccess,
            onError = {
                val exception = exceptionConverter.convert(it)
                onError(exception)
            },
        )
    }

    protected inline fun <T> AppResult<T>.handleResult(
        onLoading: AppResult<T>.() -> Unit = {},
        onUnauthorized: AppResult<T>.(exception: AppException) -> Unit,
        onError: AppResult<T>.(exception: AppException) -> Unit,
        onSuccess: AppResult<T>.(value: T) -> Unit = {},
    ) {
        fold(
            onLoading = onLoading,
            onSuccess = onSuccess,
            onError = {
                val exception = exceptionConverter.convert(it)
                if (exception.isUnauthorized) {
                    onUnauthorized(exception)
                } else {
                    onError(exception)
                }
            },
        )
    }

    protected suspend inline fun <T> Flow<AppResult<T>>.handleResult(
        crossinline onLoading: AppResult<T>.() -> Unit = {},
        crossinline onError: AppResult<T>.(exception: AppException) -> Unit = {},
        crossinline onSuccess: AppResult<T>.(value: T) -> Unit = {},
    ) {
        collect {
            it.handleResult(
                onLoading,
                onError,
                onSuccess,
            )
        }
    }

    protected suspend inline fun <T> Flow<AppResult<T>>.handleResult(
        crossinline onLoading: AppResult<T>.() -> Unit = {},
        crossinline onUnauthorized: AppResult<T>.(exception: AppException) -> Unit,
        crossinline onError: AppResult<T>.(exception: AppException) -> Unit,
        crossinline onSuccess: AppResult<T>.(value: T) -> Unit = {},
    ) {
        collect {
            it.handleResult(
                onLoading,
                onUnauthorized,
                onError,
                onSuccess,
            )
        }
    }


    protected suspend fun <T> Flow<AppResult<T>>.emitResultTo(
        stateFlow: MutableStateFlow<UIState<T>?>,
        //TODO replace with lambda builder pattern
        retryAction: (() -> Unit)? = null,
        handleUnauthorized: Boolean = true,
    ) {
        collect {
            if (handleUnauthorized) {
                it.handleResult(
                    onLoading = {
                        stateFlow.value = UIState.Loading(additionalData)
                    },
                    onUnauthorized = { handleUnauthorized(it) },
                    onError = { exception ->
                        stateFlow.value = UIState.Error(exception, retryAction, additionalData)
                    },
                    onSuccess = { data ->
                        stateFlow.value = UIState.Success(data, additionalData)
                    },
                )
            } else {
                it.handleResult(
                    onLoading = {
                        stateFlow.value = UIState.Loading(additionalData)
                    },
                    onError = { exception ->
                        stateFlow.value = UIState.Error(exception, retryAction, additionalData)
                    },
                    onSuccess = { data ->
                        stateFlow.value = UIState.Success(data, additionalData)
                    },
                )
            }
        }
    }

    protected fun handleUnauthorized(exception: AppException) {
        //TODO restart app
    }


    protected suspend fun <I, O> UseCase<I, O>.runAndEmitResultTo(
        input: I,
        stateFlow: MutableStateFlow<UIState<O>?>,
        //TODO replace with lambda builder pattern
        retryAction: (() -> Unit)? = null,
        handleUnauthorized: Boolean = true,
    ) = useCaseFlow(this, input).emitResultTo(stateFlow, retryAction, handleUnauthorized)

    protected suspend fun <O> UseCase<Unit, O>.runAndEmitResultTo(
        stateFlow: MutableStateFlow<UIState<O>?>,
        //TODO replace with lambda builder pattern
        retryAction: (() -> Unit)? = null,
        handleUnauthorized: Boolean = true,
    ) = useCaseFlow(this).emitResultTo(stateFlow, retryAction, handleUnauthorized)
}