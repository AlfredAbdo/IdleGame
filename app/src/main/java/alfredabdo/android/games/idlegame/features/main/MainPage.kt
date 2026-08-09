package alfredabdo.android.games.idlegame.features.main

import alfredabdo.android.games.idlegame.R
import alfredabdo.android.games.idlegame.features.MainRoute
import alfredabdo.android.games.idlegame.features.game.GamePage
import alfredabdo.android.games.idlegame.features.home.HomePage
import alfredabdo.android.games.idlegame.features.login.LoginPage
import alfredabdo.android.games.idlegame.features.settings.SettingsPage
import alfredabdo.android.games.idlegame.ui.bars.MainTopAppBar
import alfredabdo.android.games.idlegame.ui.bars.NavBackIcon
import alfredabdo.android.games.idlegame.ui.navdisplay.BottomSheetSceneStrategy
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay

@Composable
fun MainPage(
    redirectToHome: Boolean,
    onQuitApp: () -> Unit,
) {
    val backStack = rememberNavBackStack(if (redirectToHome) MainRoute.Home else MainRoute.Login)
    val dialogStrategy = remember { DialogSceneStrategy<NavKey>() }
    val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>() }

    val currentRoute: MainRoute by remember {
        derivedStateOf {
            backStack.lastOrNull() as? MainRoute ?: (if (redirectToHome) MainRoute.Home else MainRoute.Login)
        }
    }


    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            MainTopAppBar(
                title = {
                    Text(
                        when (currentRoute) {
                            is MainRoute.Login -> stringResource(R.string.login)
                            is MainRoute.Home -> stringResource(R.string.home)
                            is MainRoute.Game -> stringResource(R.string.game)
                            is MainRoute.Settings -> stringResource(R.string.settings)
                        }
                    )
                },
                navigationIcon = {
                    if (currentRoute !is MainRoute.Home) {
                        NavBackIcon(
                            onClick = {
                                backStack.removeLastOrNull()
                            },
                        )
                    }
                },
            )
        },
    ) { padding ->
        NavDisplay(
            backStack,
            Modifier
                .consumeWindowInsets(padding)
                .padding(padding)
                .fillMaxSize(),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            sceneStrategies = listOf(
                bottomSheetStrategy,
                dialogStrategy,
            ),
            entryProvider = entryProvider {
                entry<MainRoute.Login> {
                    LoginPage(
                        onGoToHome = {
                            backStack.removeLastOrNull()
                            backStack += MainRoute.Home
                        },
                    )
                }

                entry<MainRoute.Home> {
                    HomePage(
                        onGoToGame = {
                            backStack += MainRoute.Game
                        },
                        onGoToSettings = {
                            backStack += MainRoute.Settings
                        },
                        onQuitApp = onQuitApp,
                    )
                }

                entry<MainRoute.Game> {
                    GamePage(
                        //...
                    )
                }

                entry<MainRoute.Settings> {
                    SettingsPage(
                        //...
                    )
                }
            },
        )
    }
}