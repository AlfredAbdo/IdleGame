package alfredabdo.android.games.idlegame.data.exception

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    @SerialName("code") val code: Int?,
    @SerialName("message") val sourceMessage: String?,
)