package alfredabdo.android.games.idlegame.util.game

import alfredabdo.android.games.idlegame.data.GameItem
import alfredabdo.android.games.idlegame.data.GameItem.UpgradeMultipliers
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object GameCalculator {
    /**
     * @param base Item's [base upgrade cost][GameItem.baseUpgradeCost].
     * @param multiplier Upgrade's [cost multiplier][UpgradeMultipliers.costMultiplier].
     * @param level The level of the item after the upgrade.
     */
    fun calculateCost(base: Double, multiplier: Double, level: Int): Double {
        val roundingFactor = getRoundingFactor(base)
        return (base * multiplier.pow(level - 1)).roundToInt() / roundingFactor * roundingFactor.toDouble()
    }

    /**
     * @param base Item's [base fill rate][GameItem.baseFillRate].
     * @param multiplier Upgrade's [fill rate multiplier][UpgradeMultipliers.fillRateMultiplier].
     * @param level The level of the item after the upgrade.
     */
    fun calculateFillRate(base: Duration, multiplier: Double, level: Int): Duration {
        return (base * multiplier.pow(1 - level)).coerceAtLeast(100.milliseconds)
    }

    /**
     * @param base Item's [base gain][GameItem.baseGain].
     * @param multiplier Upgrade's [gain multiplier][UpgradeMultipliers.gainMultiplier].
     * @param level The level of the item after the upgrade.
     */
    fun calculateGain(base: Double, multiplier: Double, level: Int): Double {
        val roundingFactor = getRoundingFactor(base)
        return (base * multiplier.pow(level - 1)).roundToInt() / roundingFactor * roundingFactor.toDouble()
    }


    private fun getRoundingFactor(base: Double): Int = when {
        base < 20.0 -> 1
        base < 50.0 -> 5
        else -> 10
    }
}