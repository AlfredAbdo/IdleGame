package alfredabdo.android.games.idlegame.data

import alfredabdo.android.games.idlegame.data.api.AchievementDTO
import androidx.compose.runtime.Immutable

@Immutable
class GameAchievement(
    val id: String,
    val text: String,
    val popup: String,
    val condition: (
        coins: Double,
        itemStates: Map<GameItem, GameItemState>,
    ) -> Boolean,
) {
    companion object {
        fun fromDTO(dto: AchievementDTO) = GameAchievement(
            dto.id.toString(),
            dto.description.orEmpty(),
            dto.popupText.orEmpty(),
            condition = { coins, itemStates ->
                dto.conditions
                    ?.all { condition -> condition.checkCondition(coins, itemStates) }
                    ?: true
            },
        )


        private fun AchievementDTO.Condition.checkCondition(
            coins: Double,
            itemStates: Map<GameItem, GameItemState>,
        ): Boolean = when {
            coinsCondition != null -> coinsCondition.checkValue(coins)

            itemsCondition != null -> {
                when (val index = itemsCondition.index) {
                    AchievementDTO.Condition.ItemConditionData.ANY_INDEX ->
                        itemStates.entries
                            .any { (item, state) -> itemsCondition.checkCondition(item, state) }

                    AchievementDTO.Condition.ItemConditionData.ALL_INDEX ->
                        itemStates.entries
                            .all { (item, state) -> itemsCondition.checkCondition(item, state) }

                    else ->
                        itemStates.entries.elementAtOrNull(index)
                            ?.let { (item, state) -> itemsCondition.checkCondition(item, state) }
                            ?: false
                }
            }

            else -> false
        }

        private fun <T> AchievementDTO.Condition.ValueComparison<T>.checkValue(value: T): Boolean
                where T : Number, T : Comparable<T> =
            when (this) {
                is AchievementDTO.Condition.ValueComparison.LessThan<T> -> value < this.value
                is AchievementDTO.Condition.ValueComparison.LessThanOrEqual<T> -> value <= this.value
                is AchievementDTO.Condition.ValueComparison.Equals<T> -> value == this.value
                is AchievementDTO.Condition.ValueComparison.GreaterThanOrEqual<T> -> value >= this.value
                is AchievementDTO.Condition.ValueComparison.GreaterThan<T> -> value == this.value
            }

        private fun AchievementDTO.Condition.ItemConditionData.checkCondition(
            item: GameItem,
            itemState: GameItemState,
        ): Boolean {
            return when {
                level != null -> level.checkValue(itemState.level)
                unlocked != null -> itemState.unlocked == unlocked
                fillRateMs != null -> fillRateMs.checkValue(itemState.fillRate.inWholeMilliseconds)
                gain != null -> gain.checkValue(itemState.gain)
                upgradeCost != null -> upgradeCost.checkValue(itemState.upgradeCost)
                else -> false
            }
        }
    }
}