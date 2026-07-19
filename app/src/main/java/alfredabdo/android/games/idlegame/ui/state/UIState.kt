package alfredabdo.android.games.idlegame.ui.state

import alfredabdo.android.games.idlegame.data.exception.AppException

sealed interface UIState<out T> {
    class Loading(
        val additionalData: Map<Any, Any?> = emptyMap(),
    ) : UIState<Nothing>

    class Error(
        val exception: AppException,
        val retryAction: (() -> Unit)? = null,
        val additionalData: Map<Any, Any?> = emptyMap(),
    ) : UIState<Nothing>

    class Success<T>(
        val data: T,
        val additionalData: Map<Any, Any?> = emptyMap(),
    ) : UIState<T>
}