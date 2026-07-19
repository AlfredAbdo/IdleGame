package alfredabdo.android.games.idlegame.features.game.data

import alfredabdo.android.games.idlegame.base.UseCase
import alfredabdo.android.games.idlegame.data.GameItemSavedState
import alfredabdo.android.games.idlegame.data.GameSavedState
import alfredabdo.android.games.idlegame.data.repo.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

class SaveGameStateUseCase(
    private val repo: GameRepository,
) : UseCase<HomeGameState, Unit>(Dispatchers.Default) {

    override suspend fun performInvoke(input: HomeGameState) {
//        delay(2.seconds) //just to see the animation
        val state = GameSavedState(
            input.coins,
            input.states.mapValues { (_, value) ->
                GameItemSavedState(
                    value.id,
                    value.level,
                    value.unlocked,
                    value.fillRate,
                    value.gain,
                    value.upgradeCost,
                    value.progress,
                )
            },
            input.activeAchievement,
        )
        repo.saveGameState(state)
    }
}