package alfredabdo.android.games.idlegame.features.login.data

import alfredabdo.android.games.idlegame.base.UseCase
import alfredabdo.android.games.idlegame.data.repo.GameRepository

class LoginUseCase(
    private val gameRepo: GameRepository,
) : UseCase<LoginUseCase.Params, String>() {

    override suspend fun performInvoke(input: Params): String {
        return if (input.shouldCarryLocalSaveOver && gameRepo.hasLocalSave()) {
            gameRepo.getOrCreateSessionWithLocalSaveFor(input.username)
        } else {
            gameRepo.getOrCreateSessionFor(input.username)
        }
    }

    class Params(
        val username: String,
        val shouldCarryLocalSaveOver: Boolean,
    )
}