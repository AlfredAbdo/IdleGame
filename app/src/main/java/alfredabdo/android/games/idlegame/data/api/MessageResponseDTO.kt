package alfredabdo.android.games.idlegame.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MessageResponseDTO(
    @SerialName("message") val message: String?,
)