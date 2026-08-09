package alfredabdo.android.games.idlegame.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GameItemStateDTO(
    @SerialName("level") val level: Int,
    @SerialName("unlocked") val unlocked: Boolean?,
    @SerialName("fillRateMs") val fillRateMs: Long?,
    @SerialName("gain") val gain: Double?,
    @SerialName("upgradeCost") val upgradeCost: Double?,
    @SerialName("progress") val progress: Double?,
)