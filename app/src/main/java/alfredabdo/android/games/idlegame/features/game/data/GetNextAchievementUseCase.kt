package alfredabdo.android.games.idlegame.features.game.data

import alfredabdo.android.games.idlegame.base.UseCase
import alfredabdo.android.games.idlegame.data.GameAchievement
import alfredabdo.android.games.idlegame.data.repo.GameRepository
import kotlinx.coroutines.Dispatchers

class GetNextAchievementUseCase(
    private val repo: GameRepository,
) : UseCase<String?, GameAchievement?>(Dispatchers.Default) {

    override suspend fun performInvoke(input: String?): GameAchievement? {
        return repo.getNextAchievement(input)
    }
}