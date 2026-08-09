package alfredabdo.android.games.idlegame.data.exception

import android.content.Context
import kotlinx.serialization.json.Json

class AppExceptionConverter(
    private val context: Context, //fixme pass new resources instead
    private val json: Json,
) {

    fun convert(throwable: Throwable): AppException {
        return when (throwable) {
            is AppException -> throwable

            //TODO handle execption from ApiClient

            else -> AppException(
                AppException.Codes.UNKNOWN,
                throwable.message.orEmpty(),
                throwable,
            )
        }
    }
}