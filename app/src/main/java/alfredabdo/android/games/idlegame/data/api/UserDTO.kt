package alfredabdo.android.games.idlegame.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UserDTO(
    @SerialName("userId") val userId: UInt,
    @SerialName("username") val username: String?,
    @SerialName("coins") val coins: Double?,
    @SerialName("states") val states: Map<String, GameItemStateDTO>?,
    @SerialName("activeAchievement") val activeAchievement: AchievementDTO?,
)