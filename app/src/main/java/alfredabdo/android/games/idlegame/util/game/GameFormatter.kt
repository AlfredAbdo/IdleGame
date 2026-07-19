package alfredabdo.android.games.idlegame.util.game

import java.text.DecimalFormat
import kotlin.time.Duration

object GameFormatter {
    private val coinsFormat = DecimalFormat("#,###,##0")
    private val amountFormat = DecimalFormat("#,###,##0")


    fun formatCoins(coins: Double): String = coinsFormat.format(coins)

    fun formatAmount(amount: Double): String = amountFormat.format(amount)

    fun formatDuration(duration: Duration): String = duration.toComponents { hours, minutes, seconds, nanoseconds ->
        buildString {
            val hasHours = hours != 0L
            val hasMinutes = minutes != 0
            val hasSeconds = seconds != 0
            val hasMilliseconds = nanoseconds != 0

            var components = 0
            if (hasHours) {
                append(hours).append('h')
                components++
            }
            if (
                hasMinutes ||
                ((hasSeconds || hasMilliseconds) && hasHours)
            ) {
                if (components++ > 0) append(' ')
                append(minutes).append('m')
            }
            if (
                hasSeconds ||
                (hasMilliseconds && (hasMinutes || hasHours))
            ) {
                if (components++ > 0) append(' ')
                append(seconds).append('s')
            }
            if (hasMilliseconds) {
                if (components > 0) append(' ')
                append((nanoseconds / 1_000_000).toString().padStart(3, '0')).append("ms")
            }
        }
    }
}