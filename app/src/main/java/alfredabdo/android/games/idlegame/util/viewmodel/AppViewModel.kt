package alfredabdo.android.games.idlegame.util.viewmodel

import alfredabdo.android.games.idlegame.base.viewmodel.appViewModelFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalWithComputedDefaultOf
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified VM : ViewModel> appViewModel(
    viewModelStoreOwner: ViewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        },
    key: String? = null,
    extras: CreationExtras =
        if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
            viewModelStoreOwner.defaultViewModelCreationExtras
        } else {
            CreationExtras.Empty
        },
): VM = viewModel(viewModelStoreOwner, key, LocalAppViewModelFactory.current, extras)

@Composable
fun ProvidesAppViewModelFactory(
    viewModelFactory: ViewModelProvider.Factory,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAppViewModelFactory provides viewModelFactory,
        content = content,
    )
}

val LocalAppViewModelFactory = compositionLocalWithComputedDefaultOf { appViewModelFactory() }