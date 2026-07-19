package alfredabdo.android.games.idlegame.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class UseCase<I, O>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    suspend operator fun invoke(input: I): O = withContext(coroutineDispatcher) { performInvoke(input) }

    abstract suspend fun performInvoke(input: I): O
}