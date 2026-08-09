package alfredabdo.android.games.idlegame.data.session

import alfredabdo.android.games.idlegame.data.GameItemSavedState
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    @SerialName("a") val coins: Double,
    @SerialName("b") val gameItemStates: Map<String, GameItemSavedState>,
    @SerialName("c") val activeAchievementId: String?,
    @SerialName("d") val userId: UInt?,
    @SerialName("e") val username: String?,
) {
    companion object {
        val default = Session(
            coins = 0.0,
            gameItemStates = emptyMap(),
            activeAchievementId = USE_FIRST_ACHIEVEMENT_ID,
            userId = null,
            username = null,
        )

        const val USE_FIRST_ACHIEVEMENT_ID = "null"
    }
}