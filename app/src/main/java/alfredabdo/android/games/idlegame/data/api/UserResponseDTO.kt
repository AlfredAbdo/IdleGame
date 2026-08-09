package alfredabdo.android.games.idlegame.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserResponseDTO(
    @SerialName("user") val user: UserDTO?,
    @SerialName("gameItems") val gameItems: List<GameItemDTO>?,
    @SerialName("achievements") val achievements: List<AchievementDTO>?,
)