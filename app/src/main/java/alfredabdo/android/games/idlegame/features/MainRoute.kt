package alfredabdo.android.games.idlegame.features

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface MainRoute : NavKey {

    @Serializable
    data object Login : MainRoute

    @Serializable
    data object Home : MainRoute

    @Serializable
    data object Game : MainRoute

    @Serializable
    data object Settings : MainRoute
}