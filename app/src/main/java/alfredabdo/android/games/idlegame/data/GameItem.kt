package alfredabdo.android.games.idlegame.data

import kotlin.time.Duration

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
}