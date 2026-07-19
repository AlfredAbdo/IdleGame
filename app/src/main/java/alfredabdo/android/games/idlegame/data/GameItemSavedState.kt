package alfredabdo.android.games.idlegame.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
class GameItemSavedState(
    @SerialName("a") override val id: String,
    @SerialName("b") override val level: Int,
    @SerialName("c") override val unlocked: Boolean,
    @SerialName("d") override val fillRate: Duration,
    @SerialName("e") override val gain: Double,
    @SerialName("f") override val upgradeCost: Double,
    @SerialName("g") override val progress: Double = 0.0,
) : GameItemState