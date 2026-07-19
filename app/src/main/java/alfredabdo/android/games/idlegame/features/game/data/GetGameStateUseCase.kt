package alfredabdo.android.games.idlegame.features.game.data

import alfredabdo.android.games.idlegame.base.UseCase
import alfredabdo.android.games.idlegame.data.GameItem
import alfredabdo.android.games.idlegame.data.repo.GameRepository
import androidx.compose.runtime.mutableStateMapOf
import kotlinx.coroutines.Dispatchers

class GetGameStateUseCase(
    private val repo: GameRepository,
) : UseCase<Unit, HomeGameState>(Dispatchers.Default) {

    override suspend fun performInvoke(input: Unit): HomeGameState {
        return repo.getGameState().let { state ->
            HomeGameState(
                state.coins,
                mutableStateMapOf<GameItem, HomeGameItemState>().apply {
                    state.states.forEach { (item, state) ->
                        this[item] = HomeGameItemState(
                            state.id,
                            state.level,
                            state.unlocked,
                            state.fillRate,
                            state.gain,
                            state.upgradeCost,
                            state.progress,
                        )
                    }
                },
                state.activeAchievement,
            )
        }
    }
}