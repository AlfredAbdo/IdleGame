package alfredabdo.android.games.idlegame.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDTO(
    @SerialName("id") val id: Long,
    @SerialName("user") val user: UserDTO?,
) {
    @Serializable
    data class UserDTO(
        @SerialName("id") val id: Long,
        @SerialName("username") val username: String?,
    )
}