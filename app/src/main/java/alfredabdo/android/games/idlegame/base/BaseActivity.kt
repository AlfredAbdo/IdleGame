package alfredabdo.android.games.idlegame.base

import alfredabdo.android.games.idlegame.base.viewmodel.appViewModelFactory
import alfredabdo.android.games.idlegame.util.viewmodel.ProvidesAppViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.CallSuper
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider

abstract class BaseActivity : ComponentActivity() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }


    protected val appViewModelFactory: ViewModelProvider.Factory by lazy { appViewModelFactory() }


    @Composable
    protected inline fun ProvidesDefaultAppViewModelFactory(
        crossinline content: @Composable () -> Unit,
    ) {
        ProvidesAppViewModelFactory(appViewModelFactory) { content() }
    }
}