package alfredabdo.android.games.idlegame.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GameItemDTO(
    @SerialName("id") val id: UInt,
    @SerialName("title") val title: String?,
    @SerialName("description") val description: String?,
    @SerialName("baseFillRateMs") val baseFillRateMs: Long?,
    @SerialName("baseGain") val baseGain: Double?,
    @SerialName("upgradeMultipliers") val upgradeMultipliers: UpgradeMultipliers?,
    @SerialName("unlockAmount") val unlockAmount: Double?,
    @SerialName("baseUpgradeCost") val baseUpgradeCost: Double?,
) {
    @Serializable
    class UpgradeMultipliers(
        @SerialName("costMultiplier") val costMultiplier: Double?,
        @SerialName("fillRateMultiplier") val fillRateMultiplier: Double?,
        @SerialName("gainMultiplier") val gainMultiplier: Double?,
    )
}