package alfredabdo.android.games.idlegame.data

import alfredabdo.android.games.idlegame.data.api.GameItemDTO
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

data class GameItem(
    val id: String,
    val title: String,
    val description: String,
    val baseFillRate: Duration,
    val baseGain: Double,
    val upgradeMultipliers: UpgradeMultipliers,
    val unlockAmount: Double?,
    val baseUpgradeCost: Double,
) {
    override fun equals(other: Any?): Boolean =
        other is GameItem &&
                this.id == other.id

    override fun hashCode(): Int = id.hashCode()


    data class UpgradeMultipliers(
        val costMultiplier: Double,
        val fillRateMultiplier: Double,
        val gainMultiplier: Double,
    )


    companion object {
        fun fromDTO(dto: GameItemDTO) = GameItem(
            dto.id.toString(),
            dto.title.orEmpty(),
            dto.description.orEmpty(),
            dto.baseFillRateMs?.milliseconds ?: 0.milliseconds,
            dto.baseGain ?: 0.0,
            UpgradeMultipliers(
                dto.upgradeMultipliers?.costMultiplier ?: 0.0,
                dto.upgradeMultipliers?.fillRateMultiplier ?: 0.0,
                dto.upgradeMultipliers?.gainMultiplier ?: 0.0,
            ),
            dto.unlockAmount,
            dto.baseUpgradeCost ?: 0.0,
        )
    }
}