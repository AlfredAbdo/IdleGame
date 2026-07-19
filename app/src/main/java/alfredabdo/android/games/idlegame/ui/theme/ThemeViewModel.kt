package alfredabdo.android.games.idlegame.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel : ViewModel() {

    private val _lightDarkMode = MutableStateFlow(LightDarkMode.SYSTEM)
    val lightDarkMode get() = _lightDarkMode.asStateFlow()


    fun changeLightDarkMode(mode: LightDarkMode) {
        _lightDarkMode.value = mode
    }
}

val themeViewModel
    @Composable get() = viewModel<ThemeViewModel>(
        requireNotNull(LocalView.current.findViewTreeViewModelStoreOwner()) { "Could not get ViewModelStoreOwner for ThemeViewModel" }
    )