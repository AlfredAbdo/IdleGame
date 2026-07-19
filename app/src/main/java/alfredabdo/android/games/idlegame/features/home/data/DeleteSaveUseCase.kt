package alfredabdo.android.games.idlegame.features.home.data

import alfredabdo.android.games.idlegame.base.UseCase
import alfredabdo.android.games.idlegame.data.repo.GameRepository
import kotlinx.coroutines.Dispatchers

class DeleteSaveUseCase(
    private val repo: GameRepository,
) : UseCase<Unit, Unit>(Dispatchers.Default) {

    override suspend fun performInvoke(input: Unit) {
        repo.clearGameState()
    }
}