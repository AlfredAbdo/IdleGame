package alfredabdo.android.games.idlegame.base.viewmodel

import alfredabdo.android.games.idlegame.base.inject.Injections
import alfredabdo.android.games.idlegame.features.game.GameViewModel
import alfredabdo.android.games.idlegame.features.game.data.GetGameStateUseCase
import alfredabdo.android.games.idlegame.features.game.data.GetNextAchievementUseCase
import alfredabdo.android.games.idlegame.features.game.data.SaveGameStateUseCase
import alfredabdo.android.games.idlegame.features.home.HomeViewModel
import alfredabdo.android.games.idlegame.features.home.data.DeleteSaveUseCase
import alfredabdo.android.games.idlegame.features.login.LoginViewModel
import alfredabdo.android.games.idlegame.features.login.data.LoginUseCase
import alfredabdo.android.games.idlegame.util.game.GameCalculator
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

fun appViewModelFactory() = viewModelFactory {
    initializer {
        val sessionDataStore = Injections.sessionDataStore
        val gameRepo = Injections.gameRepo
        LoginViewModel(
            sessionDataStore,
            gameRepo,
            LoginUseCase(gameRepo),
        )
    }

    initializer {
        val gameRepo = Injections.gameRepo
        HomeViewModel(
            DeleteSaveUseCase(gameRepo),
        )
    }

    initializer {
        val gameRepo = Injections.gameRepo
        GameViewModel(
            GameCalculator,
            GetGameStateUseCase(gameRepo),
            SaveGameStateUseCase(gameRepo),
            GetNextAchievementUseCase(gameRepo),
        )
    }
}